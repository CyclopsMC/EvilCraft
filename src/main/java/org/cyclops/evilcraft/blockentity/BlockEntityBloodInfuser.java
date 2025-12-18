package org.cyclops.evilcraft.blockentity;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.datastructure.SingleCache;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;
import org.cyclops.cyclopscore.inventory.InventorySlotMasked;
import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.cyclopscore.inventory.slot.SlotFluidContainer;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockBloodInfuser;
import org.cyclops.evilcraft.blockentity.tickaction.EmptyFluidContainerInTankTickAction;
import org.cyclops.evilcraft.blockentity.tickaction.bloodinfuser.FluidContainerItemTickAction;
import org.cyclops.evilcraft.blockentity.tickaction.bloodinfuser.InfuseItemTickAction;
import org.cyclops.evilcraft.core.blockentity.BlockEntityWorking;
import org.cyclops.evilcraft.core.blockentity.tickaction.ITickAction;
import org.cyclops.evilcraft.core.blockentity.tickaction.TickComponent;
import org.cyclops.evilcraft.core.blockentity.upgrade.IUpgradeSensitiveEvent;
import org.cyclops.evilcraft.core.blockentity.upgrade.UpgradeBehaviour;
import org.cyclops.evilcraft.core.blockentity.upgrade.Upgrades;
import org.cyclops.evilcraft.core.fluid.BloodFluidConverter;
import org.cyclops.evilcraft.core.fluid.ImplicitFluidConversionTank;
import org.cyclops.evilcraft.core.recipe.type.IInventoryFluidTier;
import org.cyclops.evilcraft.core.recipe.type.InventoryFluidTier;
import org.cyclops.evilcraft.core.recipe.type.RecipeBloodInfuser;
import org.cyclops.evilcraft.inventory.container.ContainerBloodInfuser;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * A machine that can infuse things with blood.
 * @author rubensworks
 *
 */
public class BlockEntityBloodInfuser extends BlockEntityWorking<BlockEntityBloodInfuser, MutableInt> implements MenuProvider {

    /**
     * The total amount of slots in this machine.
     */
    public static final int SLOTS = 3;
    /**
     * The id of the fluid container drainer slot.
     */
    public static final int SLOT_CONTAINER = 0;
    /**
     * The id of the infusion slot.
     */
    public static final int SLOT_INFUSE = 1;
    /**
     * The id of the infusion result slot.
     */
    public static final int SLOT_INFUSE_RESULT = 2;
    /**
     * The capacity of the tank.
     */
    public static final int LIQUID_PER_SLOT = IModHelpersNeoForge.get().getFluidHelpers().getBucketVolume() * 10;

    public static Metadata METADATA = new Metadata();

    private int infuseTicker;
    private SingleCache<Triple<ItemStack, Integer, Integer>, Optional<RecipeBloodInfuser>> recipeCache;
    private float xp;

    private static final Multimap<Class<?>, ITickAction<BlockEntityBloodInfuser>> INFUSE_TICK_ACTIONS = LinkedListMultimap.create();
    static {
        INFUSE_TICK_ACTIONS.put(Item.class, new FluidContainerItemTickAction());
        INFUSE_TICK_ACTIONS.put(Item.class, new InfuseItemTickAction());
    }

    private static final Map<Class<?>, ITickAction<BlockEntityBloodInfuser>> EMPTY_IN_TANK_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickAction<BlockEntityBloodInfuser>>();
    static {
        EMPTY_IN_TANK_TICK_ACTIONS.put(Item.class, new EmptyFluidContainerInTankTickAction<BlockEntityBloodInfuser>());
    }
    public static int TICKERS = 2;

    public static final Upgrades.UpgradeEventType UPGRADEEVENT_SPEED = Upgrades.newUpgradeEventType();
    public static final Upgrades.UpgradeEventType UPGRADEEVENT_BLOODUSAGE = Upgrades.newUpgradeEventType();
    public static final Upgrades.UpgradeEventType UPGRADEEVENT_FILLBLOODPERTICK = Upgrades.newUpgradeEventType();

    public BlockEntityBloodInfuser(BlockPos blockPos, BlockState blockState) {
        super(RegistryEntries.BLOCK_ENTITY_BLOOD_INFUSER.get(), blockPos, blockState, SLOTS, 64, LIQUID_PER_SLOT, RegistryEntries.FLUID_BLOOD.get());
        infuseTicker = addTicker(new TickComponent<>(this, INFUSE_TICK_ACTIONS, SLOT_INFUSE));
        addTicker(new TickComponent<>(this, EMPTY_IN_TANK_TICK_ACTIONS, SLOT_CONTAINER, false, true));
        assert getTickers().size() == TICKERS;

        // Upgrade behaviour
        upgradeBehaviour.put(Upgrades.UPGRADE_EFFICIENCY, new UpgradeBehaviour<BlockEntityBloodInfuser, MutableInt>(2) {
            @Override
            public void applyUpgrade(BlockEntityBloodInfuser upgradable, Upgrades.Upgrade upgrade, int upgradeLevel,
                                     IUpgradeSensitiveEvent<MutableInt> event) {
                if(event.getType() == UPGRADEEVENT_BLOODUSAGE) {
                    int val = event.getObject().getValue();
                    val /= (1 + upgradeLevel / valueFactor);
                    event.getObject().setValue(val);
                }
            }
        });
        upgradeBehaviour.put(Upgrades.UPGRADE_SPEED, new UpgradeBehaviour<BlockEntityBloodInfuser, MutableInt>(1) {
            @Override
            public void applyUpgrade(BlockEntityBloodInfuser upgradable, Upgrades.Upgrade upgrade, int upgradeLevel,
                                     IUpgradeSensitiveEvent<MutableInt> event) {
                if(event.getType() == UPGRADEEVENT_FILLBLOODPERTICK) {
                    int val = event.getObject().getValue();
                    val *= (1 + upgradeLevel / valueFactor);
                    event.getObject().setValue(val);
                } else if(event.getType() == UPGRADEEVENT_SPEED) {
                    int val = event.getObject().getValue();
                    val /= (1 + upgradeLevel / valueFactor);
                    event.getObject().setValue(val);
                }
            }
        });

        // Efficient cache to retrieve the current craftable recipe.
        recipeCache = new SingleCache<>(
                new SingleCache.ICacheUpdater<Triple<ItemStack, Integer, Integer>, Optional<RecipeBloodInfuser>>() {
                    @Override
                    public Optional<RecipeBloodInfuser> getNewValue(Triple<ItemStack, Integer, Integer> key) {
                        IInventoryFluidTier recipeInput = new InventoryFluidTier(
                                NonNullList.of(ItemStack.EMPTY, key.getLeft()),
                                NonNullList.of(FluidStack.EMPTY, new FluidStack(RegistryEntries.FLUID_BLOOD, key.getMiddle())),
                                key.getRight());

                        // Make sure we always pick the highest tier when there are multiple matches
                        return ((ServerLevel) level).recipeAccess().recipeMap().getRecipesFor(getRegistry(), recipeInput, getLevel())
                                .map(RecipeHolder::value)
                                .max(Comparator.comparingInt(r -> r.getInputTier().orElse(0)));
                    }

                    @Override
                    public boolean isKeyEqual(Triple<ItemStack, Integer, Integer> cacheKey, Triple<ItemStack, Integer, Integer> newKey) {
                        return cacheKey == null || newKey == null ||
                                (ItemStack.matches(cacheKey.getLeft(), newKey.getLeft()) &&
                                        cacheKey.getMiddle().equals(newKey.getMiddle()) &&
                                        cacheKey.getRight().equals(newKey.getRight()));
                    }
                });
    }

    public static class CapabilityRegistrar extends BlockEntityWorking.CapabilityRegistrar<BlockEntityBloodInfuser, MutableInt> {
        public CapabilityRegistrar(Supplier<BlockEntityType<? extends BlockEntityBloodInfuser>> blockEntityType) {
            super(blockEntityType);
        }

        @Override
        public void registerTankInventoryCapabilitiesItem() {
            add(
                    net.neoforged.neoforge.capabilities.Capabilities.Item.BLOCK,
                    (blockEntity, direction) -> {
                        int slot = SLOT_CONTAINER;
                        if (direction == Direction.UP) {
                            slot = SLOT_INFUSE;
                        }
                        if (direction == Direction.DOWN) {
                            slot = SLOT_INFUSE_RESULT;
                        }
                        return VanillaContainerWrapper.of(new InventorySlotMasked(blockEntity.getInventory(), slot));
                    }
            );
        }
    }

    @Override
    protected SimpleInventory createInventory(int inventorySize, int stackSize) {
        return new Inventory(inventorySize, stackSize, this);
    }

    @Override
    public void saveAdditional(ValueOutput valueOutput) {
        valueOutput.putFloat("xp", xp);
        super.saveAdditional(valueOutput);
    }

    @Override
    public void read(ValueInput valueInput) {
        this.xp = valueInput.getFloatOr("xp", 0);
        super.read(valueInput);
    }

    @Override
    public Direction getRotation() {
        return IModHelpers.get().getBlockHelpers().getSafeBlockStateProperty(getBlockState(), BlockBloodInfuser.FACING, Direction.NORTH).getOpposite();
    }

    @Override
    protected SingleUseTank createTank(int tankSize) {
        return new ImplicitFluidConversionTank(tankSize, BloodFluidConverter.getInstance());
    }

    public Optional<RecipeBloodInfuser> getRecipe(ItemStack itemStack) {
        return recipeCache.get(Triple.of(
                itemStack.isEmpty() ? ItemStack.EMPTY : itemStack.copy(),
                getTank().getFluidAmount(),
                getTileWorkingMetadata().getTier(getInventory())));
    }

    @Override
    public void onStateChanged() {
        sendUpdate();
        level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(BlockBloodInfuser.ON, isWorking()));
        IModHelpers.get().getBlockHelpers().markForUpdate(getLevel(), getBlockPos());
    }

    @Override
    public Metadata getTileWorkingMetadata() {
        return METADATA;
    }

    @Override
    public boolean canWork() {
        return true;
    }

    @Override
    protected int getWorkTicker() {
        return infuseTicker;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, net.minecraft.world.entity.player.Inventory playerInventory, Player playerEntity) {
        return new ContainerBloodInfuser(id, playerInventory, this.getInventory(), Optional.of(this));
    }

    public RecipeType<RecipeBloodInfuser> getRegistry() {
        return RegistryEntries.RECIPETYPE_BLOOD_INFUSER.get();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.evilcraft.blood_infuser");
    }

    public void setXp(float xp) {
        this.xp = xp;
        setChanged();
    }

    public float getXp() {
        return xp;
    }

    public void addXp(float xp) {
        setXp(getXp() + xp);
    }

    public void resetXp() {
        setXp(0);
    }

    public static class Metadata extends BlockEntityWorking.Metadata {
        private Metadata() {
            super(SLOTS);
        }

        @Override
        public boolean canConsume(ItemStack itemStack, Level world) {
            // Valid fluid handler
            if (!itemStack.isEmpty()) {
                if (ItemAccess.forStack(itemStack).getCapability(Capabilities.Fluid.ITEM) != null) {
                    return true;
                }
            }

            // Not easily possible anymore to validate recipe validity client-side, so we just accept all.
//            // Valid custom recipe
//            IInventoryFluidTier recipeInput = new InventoryFluidTier(
//                    NonNullList.of(ItemStack.EMPTY, itemStack),
//                    NonNullList.of(FluidStack.EMPTY, new FluidStack(RegistryEntries.FLUID_BLOOD, Integer.MAX_VALUE)),
//                    Upgrades.TIERS);
//            return IModHelpers.get().getCraftingHelpers().findRecipe(RegistryEntries.RECIPETYPE_BLOOD_INFUSER.get(), recipeInput, world)
//                    .isPresent();
            return true;
        }

        @Override
        public boolean canInsertItem(Container inventory, int slot, ItemStack itemStack) {
            return slot != getProduceSlot() && super.canInsertItem(inventory, slot, itemStack);
        }

        @Override
        protected Block getBlock() {
            return RegistryEntries.BLOCK_BLOOD_INFUSER.get();
        }

        /**
         * Get the id of the infusion slot.
         * @return id of the infusion slot.
         */
        public int getConsumeSlot() {
            return SLOT_INFUSE;
        }

        /**
         * Get the id of the result slot.
         * @return id of the result slot.
         */
        public int getProduceSlot() {
            return SLOT_INFUSE_RESULT;
        }
    }

    public static class Inventory extends BlockEntityWorking.Inventory<BlockEntityBloodInfuser> {

        public Inventory(int size, int stackLimit, BlockEntityBloodInfuser tile) {
            super(size, stackLimit, tile);
        }

        @Override
        public boolean canPlaceItem(int slot, ItemStack itemStack) {
            if(slot == SLOT_INFUSE)
                return tile.getTileWorkingMetadata().canConsume(itemStack, tile.getLevel());
            if(slot == SLOT_CONTAINER)
                return SlotFluidContainer.checkIsItemValid(itemStack, RegistryEntries.FLUID_BLOOD.get());
            return super.canPlaceItem(slot, itemStack);
        }
    }

}
