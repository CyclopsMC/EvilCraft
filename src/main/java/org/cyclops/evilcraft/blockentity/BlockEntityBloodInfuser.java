package org.cyclops.evilcraft.blockentity;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.capability.item.ItemHandlerSlotMasked;
import org.cyclops.cyclopscore.datastructure.SingleCache;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.FluidHelpers;
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
    public static final int LIQUID_PER_SLOT = FluidHelpers.BUCKET_VOLUME * 10;

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
        super(RegistryEntries.BLOCK_ENTITY_BLOOD_INFUSER, blockPos, blockState, SLOTS, 64, LIQUID_PER_SLOT, RegistryEntries.FLUID_BLOOD);
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
                        return level.getRecipeManager().getRecipesFor(getRegistry(), recipeInput, getLevel()).stream()
                                .max(Comparator.comparingInt(RecipeBloodInfuser::getInputTier));
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

    @Override
    protected void addItemHandlerCapabilities() {
        LazyOptional<IItemHandler> itemHandlerInfuse = LazyOptional.of(() -> new ItemHandlerSlotMasked(getInventory(), SLOT_INFUSE));
        LazyOptional<IItemHandler> itemHandlerInfuseResult = LazyOptional.of(() -> new ItemHandlerSlotMasked(getInventory(), SLOT_INFUSE_RESULT));
        LazyOptional<IItemHandler> itemHandlerContainer = LazyOptional.of(() -> new ItemHandlerSlotMasked(getInventory(), SLOT_CONTAINER));
        addCapabilitySided(ForgeCapabilities.ITEM_HANDLER, Direction.UP, itemHandlerInfuse);
        addCapabilitySided(ForgeCapabilities.ITEM_HANDLER, Direction.DOWN, itemHandlerInfuseResult);
        addCapabilitySided(ForgeCapabilities.ITEM_HANDLER, Direction.NORTH, itemHandlerContainer);
        addCapabilitySided(ForgeCapabilities.ITEM_HANDLER, Direction.SOUTH, itemHandlerContainer);
        addCapabilitySided(ForgeCapabilities.ITEM_HANDLER, Direction.WEST, itemHandlerContainer);
        addCapabilitySided(ForgeCapabilities.ITEM_HANDLER, Direction.EAST, itemHandlerContainer);
    }

    @Override
    protected SimpleInventory createInventory(int inventorySize, int stackSize) {
        return new Inventory(inventorySize, stackSize, this);
    }

    @Override
    public void saveAdditional(CompoundTag data) {
        data.putFloat("xp", xp);
        super.saveAdditional(data);
    }

    @Override
    public void read(CompoundTag data) {
        this.xp = data.getFloat("xp");
        super.read(data);
    }

    @Override
    public Direction getRotation() {
        return BlockHelpers.getSafeBlockStateProperty(getBlockState(), BlockBloodInfuser.FACING, Direction.NORTH).getOpposite();
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
        BlockHelpers.markForUpdate(getLevel(), getBlockPos());
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
        return RegistryEntries.RECIPETYPE_BLOOD_INFUSER;
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
                LazyOptional<IFluidHandlerItem> fluidHandler = FluidUtil.getFluidHandler(itemStack.copy().split(1));
                if (fluidHandler.isPresent()) {
                    return true;
                }
            }

            // Valid custom recipe
            IInventoryFluidTier recipeInput = new InventoryFluidTier(
                    NonNullList.of(ItemStack.EMPTY, itemStack),
                    NonNullList.of(FluidStack.EMPTY, new FluidStack(RegistryEntries.FLUID_BLOOD, Integer.MAX_VALUE)),
                    Upgrades.TIERS);
            return world.getRecipeManager()
                    .getRecipeFor(RegistryEntries.RECIPETYPE_BLOOD_INFUSER, recipeInput, world)
                    .isPresent();
        }

        @Override
        public boolean canInsertItem(Container inventory, int slot, ItemStack itemStack) {
            return slot != getProduceSlot() && super.canInsertItem(inventory, slot, itemStack);
        }

        @Override
        protected Block getBlock() {
            return RegistryEntries.BLOCK_BLOOD_INFUSER;
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
                return SlotFluidContainer.checkIsItemValid(itemStack, RegistryEntries.FLUID_BLOOD);
            return super.canPlaceItem(slot, itemStack);
        }
    }

}
