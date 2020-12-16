package org.cyclops.evilcraft.tileentity;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.capability.item.ItemHandlerSlotMasked;
import org.cyclops.cyclopscore.datastructure.SingleCache;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.CraftingHelpers;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.cyclopscore.inventory.slot.SlotFluidContainer;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockBloodInfuser;
import org.cyclops.evilcraft.core.fluid.BloodFluidConverter;
import org.cyclops.evilcraft.core.fluid.ImplicitFluidConversionTank;
import org.cyclops.evilcraft.core.recipe.type.IInventoryFluidTier;
import org.cyclops.evilcraft.core.recipe.type.InventoryFluidTier;
import org.cyclops.evilcraft.core.recipe.type.RecipeBloodInfuser;
import org.cyclops.evilcraft.core.tileentity.TileWorking;
import org.cyclops.evilcraft.core.tileentity.tickaction.ITickAction;
import org.cyclops.evilcraft.core.tileentity.tickaction.TickComponent;
import org.cyclops.evilcraft.core.tileentity.upgrade.IUpgradeSensitiveEvent;
import org.cyclops.evilcraft.core.tileentity.upgrade.UpgradeBehaviour;
import org.cyclops.evilcraft.core.tileentity.upgrade.Upgrades;
import org.cyclops.evilcraft.inventory.container.ContainerBloodInfuser;
import org.cyclops.evilcraft.tileentity.tickaction.EmptyFluidContainerInTankTickAction;
import org.cyclops.evilcraft.tileentity.tickaction.bloodinfuser.FluidContainerItemTickAction;
import org.cyclops.evilcraft.tileentity.tickaction.bloodinfuser.InfuseItemTickAction;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A machine that can infuse things with blood.
 * @author rubensworks
 *
 */
public class TileBloodInfuser extends TileWorking<TileBloodInfuser, MutableInt> implements INamedContainerProvider {
    
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
    
    private static final Multimap<Class<?>, ITickAction<TileBloodInfuser>> INFUSE_TICK_ACTIONS = LinkedListMultimap.create();
    static {
        INFUSE_TICK_ACTIONS.put(Item.class, new FluidContainerItemTickAction());
        INFUSE_TICK_ACTIONS.put(Item.class, new InfuseItemTickAction());
    }
    
    private static final Map<Class<?>, ITickAction<TileBloodInfuser>> EMPTY_IN_TANK_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickAction<TileBloodInfuser>>();
    static {
        EMPTY_IN_TANK_TICK_ACTIONS.put(Item.class, new EmptyFluidContainerInTankTickAction<TileBloodInfuser>());
    }
    public static int TICKERS = 2;

    public static final Upgrades.UpgradeEventType UPGRADEEVENT_SPEED = Upgrades.newUpgradeEventType();
    public static final Upgrades.UpgradeEventType UPGRADEEVENT_BLOODUSAGE = Upgrades.newUpgradeEventType();
    public static final Upgrades.UpgradeEventType UPGRADEEVENT_FILLBLOODPERTICK = Upgrades.newUpgradeEventType();

    public TileBloodInfuser() {
        super(RegistryEntries.TILE_ENTITY_BLOOD_INFUSER, SLOTS, 64, LIQUID_PER_SLOT, RegistryEntries.FLUID_BLOOD);
        infuseTicker = addTicker(new TickComponent<>(this, INFUSE_TICK_ACTIONS, SLOT_INFUSE));
        addTicker(new TickComponent<>(this, EMPTY_IN_TANK_TICK_ACTIONS, SLOT_CONTAINER, false, true));
        assert getTickers().size() == TICKERS;

        // Upgrade behaviour
        upgradeBehaviour.put(Upgrades.UPGRADE_EFFICIENCY, new UpgradeBehaviour<TileBloodInfuser, MutableInt>(2) {
            @Override
            public void applyUpgrade(TileBloodInfuser upgradable, Upgrades.Upgrade upgrade, int upgradeLevel,
                                     IUpgradeSensitiveEvent<MutableInt> event) {
                if(event.getType() == UPGRADEEVENT_BLOODUSAGE) {
                    int val = event.getObject().getValue();
                    val /= (1 + upgradeLevel / valueFactor);
                    event.getObject().setValue(val);
                }
            }
        });
        upgradeBehaviour.put(Upgrades.UPGRADE_SPEED, new UpgradeBehaviour<TileBloodInfuser, MutableInt>(1) {
            @Override
            public void applyUpgrade(TileBloodInfuser upgradable, Upgrades.Upgrade upgrade, int upgradeLevel,
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
                                NonNullList.from(ItemStack.EMPTY, key.getLeft()),
                                NonNullList.from(FluidStack.EMPTY, new FluidStack(RegistryEntries.FLUID_BLOOD, key.getMiddle())),
                                key.getRight());

                        // Make sure we always pick the highest tier when there are multiple matches
                        return world.getRecipeManager().getRecipes(getRegistry(), recipeInput, getWorld()).stream()
                                .max(Comparator.comparingInt(RecipeBloodInfuser::getInputTier));
                    }

                    @Override
                    public boolean isKeyEqual(Triple<ItemStack, Integer, Integer> cacheKey, Triple<ItemStack, Integer, Integer> newKey) {
                        return cacheKey == null || newKey == null ||
                                (ItemStack.areItemStacksEqual(cacheKey.getLeft(), newKey.getLeft()) &&
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
        addCapabilitySided(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP, itemHandlerInfuse);
        addCapabilitySided(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.DOWN, itemHandlerInfuseResult);
        addCapabilitySided(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.NORTH, itemHandlerContainer);
        addCapabilitySided(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.SOUTH, itemHandlerContainer);
        addCapabilitySided(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.WEST, itemHandlerContainer);
        addCapabilitySided(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.EAST, itemHandlerContainer);
    }

    @Override
    protected SimpleInventory createInventory(int inventorySize, int stackSize) {
        return new Inventory(inventorySize, stackSize, this);
    }

    @Override
    public Direction getRotation() {
        return BlockHelpers.getSafeBlockStateProperty(getWorld().getBlockState(getPos()), BlockBloodInfuser.FACING, Direction.NORTH).getOpposite();
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
        world.setBlockState(getPos(), world.getBlockState(getPos()).with(BlockBloodInfuser.ON, isWorking()));
        BlockHelpers.markForUpdate(getWorld(), getPos());
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
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new ContainerBloodInfuser(id, playerInventory, this.getInventory(), Optional.of(this));
    }

    protected IRecipeType<RecipeBloodInfuser> getRegistry() {
        return RegistryEntries.RECIPETYPE_BLOOD_INFUSER;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("block.evilcraft.blood_infuser");
    }

    public static class Metadata extends TileWorking.Metadata {
        private Metadata() {
            super(SLOTS);
        }

        @Override
        public boolean canConsume(ItemStack itemStack, World world) {
            // Valid fluid handler
            if (!itemStack.isEmpty()) {
                LazyOptional<IFluidHandlerItem> fluidHandler = FluidUtil.getFluidHandler(itemStack.copy().split(1));
                if (fluidHandler.isPresent()) {
                    return true;
                }
            }

            // Valid custom recipe
            IInventoryFluidTier recipeInput = new InventoryFluidTier(
                    NonNullList.from(ItemStack.EMPTY, itemStack),
                    NonNullList.from(FluidStack.EMPTY, new FluidStack(RegistryEntries.FLUID_BLOOD, Integer.MAX_VALUE)),
                    Upgrades.TIERS);
            return world.getRecipeManager()
                    .getRecipe(RegistryEntries.RECIPETYPE_BLOOD_INFUSER, recipeInput, world)
                    .isPresent();
        }

        @Override
        public boolean canInsertItem(IInventory inventory, int slot, ItemStack itemStack) {
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

    public static class Inventory extends TileWorking.Inventory<TileBloodInfuser> {

        public Inventory(int size, int stackLimit, TileBloodInfuser tile) {
            super(size, stackLimit, tile);
        }

        @Override
        public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
            if(slot == SLOT_INFUSE)
                return tile.getTileWorkingMetadata().canConsume(itemStack, tile.getWorld());
            if(slot == SLOT_CONTAINER)
                return SlotFluidContainer.checkIsItemValid(itemStack, RegistryEntries.FLUID_BLOOD);
            return super.isItemValidForSlot(slot, itemStack);
        }
    }

}
