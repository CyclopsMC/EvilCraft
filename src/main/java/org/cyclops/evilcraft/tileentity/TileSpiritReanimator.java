package org.cyclops.evilcraft.tileentity;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.cyclops.cyclopscore.capability.item.ItemHandlerSlotMasked;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.cyclopscore.inventory.slot.SlotFluidContainer;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockBoxOfEternalClosure;
import org.cyclops.evilcraft.block.BlockSpiritReanimator;
import org.cyclops.evilcraft.core.fluid.BloodFluidConverter;
import org.cyclops.evilcraft.core.fluid.ImplicitFluidConversionTank;
import org.cyclops.evilcraft.core.tileentity.TileWorking;
import org.cyclops.evilcraft.core.tileentity.tickaction.ITickAction;
import org.cyclops.evilcraft.core.tileentity.tickaction.TickComponent;
import org.cyclops.evilcraft.core.tileentity.upgrade.IUpgradeSensitiveEvent;
import org.cyclops.evilcraft.core.tileentity.upgrade.UpgradeBehaviour;
import org.cyclops.evilcraft.core.tileentity.upgrade.Upgrades;
import org.cyclops.evilcraft.inventory.container.ContainerSpiritReanimator;
import org.cyclops.evilcraft.tileentity.tickaction.EmptyFluidContainerInTankTickAction;
import org.cyclops.evilcraft.tileentity.tickaction.spiritreanimator.ReanimateTickAction;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A furnace that is able to cook spirits for their inner entity drops.
 * @author rubensworks
 *
 */
public class TileSpiritReanimator extends TileWorking<TileSpiritReanimator, MutableDouble> implements INamedContainerProvider {

    public static Metadata METADATA = new Metadata();
    
    /**
     * The id of the fluid container drainer slot.
     */
    public static final int SLOT_CONTAINER = 0;
    /**
     * The id of the box slot.
     */
    public static final int SLOT_BOX = 1;
    /**
     * The id of the egg slot.
     */
    public static final int SLOT_EGG = 2;
    /**
     * The id of the output slot.
     */
    public static final int SLOTS_OUTPUT = 3;
    
    /**
     * The total amount of slots in this machine.
     */
    public static final int SLOTS = 4;

    /**
     * The capacity of the tank.
     */
    public static final int LIQUID_PER_SLOT = FluidHelpers.BUCKET_VOLUME * 10;
    
    private static final Map<Class<?>, ITickAction<TileSpiritReanimator>> REANIMATE_COOK_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickAction<TileSpiritReanimator>>();
    static {
    	REANIMATE_COOK_TICK_ACTIONS.put(BlockBoxOfEternalClosure.class, new ReanimateTickAction());
    }
    
    private static final Map<Class<?>, ITickAction<TileSpiritReanimator>> EMPTY_IN_TANK_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickAction<TileSpiritReanimator>>();
    static {
        EMPTY_IN_TANK_TICK_ACTIONS.put(Item.class, new EmptyFluidContainerInTankTickAction<TileSpiritReanimator>());
    }
    public static int TICKERS = 2;

    public static final Upgrades.UpgradeEventType UPGRADEEVENT_SPEED = Upgrades.newUpgradeEventType();
    public static final Upgrades.UpgradeEventType UPGRADEEVENT_BLOODUSAGE = Upgrades.newUpgradeEventType();

    private int reanimateTicker;
    @NBTPersist
    private Boolean caughtError = false;
    
    /**
     * Make a new instance.
     */
    public TileSpiritReanimator() {
        super(
                RegistryEntries.TILE_ENTITY_SPIRIT_REANIMATOR,
                SLOTS,
                64,
                LIQUID_PER_SLOT,
                RegistryEntries.FLUID_BLOOD);
        reanimateTicker = addTicker(
                new TickComponent<
                    TileSpiritReanimator,
                    ITickAction<TileSpiritReanimator>
                >(this, REANIMATE_COOK_TICK_ACTIONS, SLOT_BOX, true, false)
                );
        addTicker(
                new TickComponent<
                    TileSpiritReanimator,
                    ITickAction<TileSpiritReanimator>
                >(this, EMPTY_IN_TANK_TICK_ACTIONS, SLOT_CONTAINER, false, true)
                );

        // Upgrade behaviour
        upgradeBehaviour.put(Upgrades.UPGRADE_SPEED, new UpgradeBehaviour<TileSpiritReanimator, MutableDouble>(1) {
            @Override
            public void applyUpgrade(TileSpiritReanimator upgradable, Upgrades.Upgrade upgrade, int upgradeLevel,
                                     IUpgradeSensitiveEvent<MutableDouble> event) {
                if(event.getType() == UPGRADEEVENT_SPEED) {
                    double val = event.getObject().getValue();
                    val /= (1 + upgradeLevel / valueFactor);
                    event.getObject().setValue(val);
                }
                if(event.getType() == UPGRADEEVENT_BLOODUSAGE) {
                    double val = event.getObject().getValue();
                    val *= (1 + upgradeLevel / valueFactor);
                    event.getObject().setValue(val);
                }
            }
        });
        upgradeBehaviour.put(Upgrades.UPGRADE_EFFICIENCY, new UpgradeBehaviour<TileSpiritReanimator, MutableDouble>(2) {
            @Override
            public void applyUpgrade(TileSpiritReanimator upgradable, Upgrades.Upgrade upgrade, int upgradeLevel,
                                     IUpgradeSensitiveEvent<MutableDouble> event) {
                if(event.getType() == UPGRADEEVENT_BLOODUSAGE) {
                    double val = event.getObject().getValue();
                    val /= (1 + upgradeLevel / valueFactor);
                    event.getObject().setValue(val);
                }
            }
        });
    }

    @Override
    protected void addItemHandlerCapabilities() {
        LazyOptional<IItemHandler> itemHandlerInput = LazyOptional.of(() -> new ItemHandlerSlotMasked(getInventory(), SLOT_BOX));
        LazyOptional<IItemHandler> itemHandlerOutput = LazyOptional.of(() -> new ItemHandlerSlotMasked(getInventory(), SLOTS_OUTPUT));
        LazyOptional<IItemHandler> itemHandlerContainer = LazyOptional.of(() -> new ItemHandlerSlotMasked(getInventory(), SLOT_CONTAINER, SLOT_EGG));
        addCapabilitySided(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP, itemHandlerInput);
        addCapabilitySided(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.DOWN, itemHandlerOutput);
        addCapabilitySided(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.NORTH, itemHandlerContainer);
        addCapabilitySided(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.SOUTH, itemHandlerContainer);
        addCapabilitySided(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.WEST, itemHandlerContainer);
        addCapabilitySided(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.EAST, itemHandlerContainer);
    }

    @Override
    protected SimpleInventory createInventory(int inventorySize, int stackSize) {
        return new Inventory<TileSpiritReanimator>(inventorySize, stackSize, this) {
            @Override
            public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
                if(slot == SLOT_BOX)
                    return getTileWorkingMetadata().canConsume(itemStack, getWorld());
                if(slot == SLOT_EGG)
                    return itemStack.getItem() == Items.EGG
                            /*&& ResurgenceEgg.getInstance().isEmpty(itemStack) also enable in acceptance slot in container*/;
                if(slot == SLOT_CONTAINER)
                    return SlotFluidContainer.checkIsItemValid(itemStack, RegistryEntries.FLUID_BLOOD);
                return super.isItemValidForSlot(slot, itemStack);
            }
        };
    }

    @Override
    public Direction getRotation() {
        return BlockHelpers.getSafeBlockStateProperty(getWorld().getBlockState(getPos()), BlockSpiritReanimator.FACING, Direction.NORTH).getOpposite();
    }
    
    @Override
    protected SingleUseTank createTank(int tankSize) {
    	return new ImplicitFluidConversionTank(tankSize, BloodFluidConverter.getInstance());
    }
    
    @Override
	protected int getWorkTicker() {
		return reanimateTicker;
	}

    /**
     * Get the entity type that is contained in a box.
     * @return The entity type or null if no box or invalid box.
     */
    @Nullable
    public EntityType<?> getEntityType() {
        ItemStack boxStack = getInventory().getStackInSlot(getConsumeSlot());
        if(boxStack.getItem() == getAllowedCookItem()) {
            return BlockBoxOfEternalClosure.getSpiritType(boxStack);
        }
        return null;
    }
    
    /**
     * Get the allowed cooking item for this furnace.
     * @return The allowed item.
     */
    public static Item getAllowedCookItem() {
        return RegistryEntries.ITEM_BOX_OF_ETERNAL_CLOSURE;
    }
    
    /**
     * Get the id of the box slot.
     * @return id of the box slot.
     */
    public int getConsumeSlot() {
        return SLOT_BOX;
    }

    @Override
    public Metadata getTileWorkingMetadata() {
        return METADATA;
    }

    @Override
	public boolean canWork() {
		ItemStack eggStack = getInventory().getStackInSlot(SLOT_EGG);
		ItemStack outputStack = getInventory().getStackInSlot(TileSpiritReanimator.SLOTS_OUTPUT);
        EntityType<?> entityType = getEntityType();
        boolean validNameStack = entityType != null
                && (outputStack.isEmpty() ||
                    (outputStack.getMaxStackSize() > outputStack.getCount()
                        && SpawnEggItem.getEgg(entityType) == outputStack.getItem()));
        return !eggStack.isEmpty() && validNameStack;
	}
	
	@Override
    public void onStateChanged() {
        world.setBlockState(getPos(), world.getBlockState(getPos()).with(BlockSpiritReanimator.ON, isWorking()));
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new ContainerSpiritReanimator(id, playerInventory, this.getInventory(), Optional.of(this));
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("block.evilcraft.spirit_reanimator");
    }

    private static class Metadata extends TileWorking.Metadata {
        private Metadata() {
            super(SLOTS);
        }

        @Override
        public boolean canConsume(ItemStack itemStack, World world) {
            return !itemStack.isEmpty() && getAllowedCookItem() == itemStack.getItem();
        }

        @Override
        protected Block getBlock() {
            return RegistryEntries.BLOCK_SPIRIT_REANIMATOR;
        }
    }

}
