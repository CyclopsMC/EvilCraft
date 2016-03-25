package org.cyclops.evilcraft.tileentity;

import net.minecraft.entity.EntityList;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.IFluidContainerItem;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.inventory.slot.SlotFluidContainer;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.block.BoxOfEternalClosure;
import org.cyclops.evilcraft.block.BoxOfEternalClosureConfig;
import org.cyclops.evilcraft.block.SpiritReanimator;
import org.cyclops.evilcraft.core.fluid.BloodFluidConverter;
import org.cyclops.evilcraft.core.fluid.ImplicitFluidConversionTank;
import org.cyclops.evilcraft.core.tileentity.tickaction.ITickAction;
import org.cyclops.evilcraft.core.tileentity.tickaction.TickComponent;
import org.cyclops.evilcraft.core.tileentity.upgrade.IUpgradeSensitiveEvent;
import org.cyclops.evilcraft.core.tileentity.upgrade.UpgradeBehaviour;
import org.cyclops.evilcraft.core.tileentity.upgrade.Upgrades;
import org.cyclops.evilcraft.fluid.Blood;
import org.cyclops.evilcraft.tileentity.tickaction.EmptyFluidContainerInTankTickAction;
import org.cyclops.evilcraft.tileentity.tickaction.EmptyItemBucketInTankTickAction;
import org.cyclops.evilcraft.tileentity.tickaction.spiritreanimator.ReanimateTickAction;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A furnace that is able to cook spirits for their inner entity drops.
 * @author rubensworks
 *
 */
public class TileSpiritReanimator extends TileWorking<TileSpiritReanimator, MutableDouble> {
    
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
     * The name of the tank, used for NBT storage.
     */
    public static String TANKNAME = "spiritReanimatorTank";
    /**
     * The capacity of the tank.
     */
    public static final int LIQUID_PER_SLOT = FluidContainerRegistry.BUCKET_VOLUME * 10;
    /**
     * The amount of ticks per mB the tank can accept per tick.
     */
    public static final int TICKS_PER_LIQUID = 2;
    /**
     * The fluid that is accepted in the tank.
     */
    public static final Fluid ACCEPTED_FLUID = Blood.getInstance();
    
    private static final Map<Class<?>, ITickAction<TileSpiritReanimator>> REANIMATE_COOK_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickAction<TileSpiritReanimator>>();
    static {
    	REANIMATE_COOK_TICK_ACTIONS.put(BoxOfEternalClosure.class, new ReanimateTickAction());
    }
    
    private static final Map<Class<?>, ITickAction<TileSpiritReanimator>> EMPTY_IN_TANK_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickAction<TileSpiritReanimator>>();
    static {
        EMPTY_IN_TANK_TICK_ACTIONS.put(IFluidContainerItem.class, new EmptyFluidContainerInTankTickAction<TileSpiritReanimator>());
        EMPTY_IN_TANK_TICK_ACTIONS.put(Item.class, new EmptyItemBucketInTankTickAction<TileSpiritReanimator>());
    }

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
                SLOTS,
                SpiritReanimator.getInstance().getLocalizedName(),
                LIQUID_PER_SLOT,
                TANKNAME,
                ACCEPTED_FLUID);
        reanimateTicker = addTicker(
                new TickComponent<
                    TileSpiritReanimator,
                    ITickAction<TileSpiritReanimator>
                >(this, REANIMATE_COOK_TICK_ACTIONS, SLOT_BOX)
                );
        addTicker(
                new TickComponent<
                    TileSpiritReanimator,
                    ITickAction<TileSpiritReanimator>
                >(this, EMPTY_IN_TANK_TICK_ACTIONS, SLOT_CONTAINER, false)
                );
        
        // The slots side mapping
        List<Integer> inSlots = new LinkedList<Integer>();
        inSlots.add(SLOT_BOX);
        inSlots.add(SLOT_EGG);
        List<Integer> inSlotsTank = new LinkedList<Integer>();
        inSlotsTank.add(SLOT_CONTAINER);
        List<Integer> outSlots = new LinkedList<Integer>();
        outSlots.add(SLOTS_OUTPUT);
        addSlotsToSide(EnumFacing.EAST, inSlotsTank);
        addSlotsToSide(EnumFacing.UP, inSlots);
        addSlotsToSide(EnumFacing.DOWN, outSlots);
        addSlotsToSide(EnumFacing.SOUTH, outSlots);
        addSlotsToSide(EnumFacing.WEST, outSlots);

        // Upgrade behaviour
        upgradeBehaviour.put(UPGRADE_SPEED, new UpgradeBehaviour<TileSpiritReanimator, MutableDouble>(1) {
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
        upgradeBehaviour.put(UPGRADE_EFFICIENCY, new UpgradeBehaviour<TileSpiritReanimator, MutableDouble>(2) {
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
    public EnumFacing getRotation() {
        return BlockHelpers.getSafeBlockStateProperty(getWorld().getBlockState(getPos()), SpiritReanimator.FACING, EnumFacing.NORTH);
    }
    
    @Override
    protected SingleUseTank newTank(String tankName, int tankSize) {
    	return new ImplicitFluidConversionTank(tankName, tankSize, this, BloodFluidConverter.getInstance());
    }
    
    @Override
	protected int getWorkTicker() {
		return reanimateTicker;
	}

    /**
     * Get the entity name that is contained in a box.
     * @return The entity or null if no box or invalid box.
     */
    public String getEntityName() {
        ItemStack boxStack = getInventory().getStackInSlot(getConsumeSlot());
        if(boxStack != null && boxStack.getItem() == getAllowedCookItem()) {
            return BoxOfEternalClosure.getInstance().getSpiritName(boxStack);
        }
        return null;
    }
    
    /**
     * Get the allowed cooking item for this furnace.
     * @return The allowed item.
     */
    public static Item getAllowedCookItem() {
    	Item allowedItem = Items.apple;
        if(Configs.isEnabled(BoxOfEternalClosureConfig.class)) {
        	allowedItem = Item.getItemFromBlock(BoxOfEternalClosure.getInstance());
        }
        return allowedItem;
    }
    
    @Override
    public boolean canConsume(ItemStack itemStack) {
        return itemStack != null && getAllowedCookItem() == itemStack.getItem();
    }
    
    /**
     * Get the id of the box slot.
     * @return id of the box slot.
     */
    public int getConsumeSlot() {
        return SLOT_BOX;
    }
    
    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        if(slot == SLOT_BOX)
            return canConsume(itemStack);
        if(slot == SLOT_EGG)
            return itemStack.getItem() == Items.egg
            	/*&& ResurgenceEgg.getInstance().isEmpty(itemStack) also enable in acceptance slot in container*/;
        if(slot == SLOT_CONTAINER)
            return SlotFluidContainer.checkIsItemValid(itemStack, getTank());
        return super.isItemValidForSlot(slot, itemStack);
    }

	@Override
	public boolean canWork() {
		ItemStack eggStack = getStackInSlot(SLOT_EGG);
		ItemStack outputStack = getStackInSlot(TileSpiritReanimator.SLOTS_OUTPUT);
        boolean validNameStack = getEntityName() != null && EntityList.entityEggs.containsKey(getEntityName())
                && (outputStack == null ||
                    (outputStack.getMaxStackSize() > outputStack.stackSize
                        && getEntityName().equals(ItemMonsterPlacer.getEntityIdFromItem(outputStack))));
        return eggStack != null /*&& ResurgenceEgg.getInstance().isEmpty(eggStack)*/
				&& validNameStack;
	}
	
	@Override
    public void onStateChanged() {
        sendUpdate();
        worldObj.setBlockState(getPos(), worldObj.getBlockState(getPos()).withProperty(SpiritReanimator.ON, isWorking()));
    }

}
