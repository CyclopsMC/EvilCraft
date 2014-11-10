package evilcraft.tileentity;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.EntityList;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.IFluidContainerItem;
import evilcraft.Configs;
import evilcraft.block.BoxOfEternalClosure;
import evilcraft.block.BoxOfEternalClosureConfig;
import evilcraft.block.SpiritReanimator;
import evilcraft.core.fluid.BloodFluidConverter;
import evilcraft.core.fluid.ImplicitFluidConversionTank;
import evilcraft.core.fluid.SingleUseTank;
import evilcraft.core.inventory.slot.SlotFluidContainer;
import evilcraft.core.tileentity.NBTPersist;
import evilcraft.core.tileentity.WorkingTileEntity;
import evilcraft.core.tileentity.tickaction.ITickAction;
import evilcraft.core.tileentity.tickaction.TickComponent;
import evilcraft.fluid.Blood;
import evilcraft.tileentity.tickaction.EmptyFluidContainerInTankTickAction;
import evilcraft.tileentity.tickaction.EmptyItemBucketInTankTickAction;
import evilcraft.tileentity.tickaction.spiritreanimator.ReanimateTickAction;

/**
 * A furnace that is able to cook spirits for their inner entity drops.
 * @author rubensworks
 *
 */
public class TileSpiritReanimator extends WorkingTileEntity<TileSpiritReanimator> {
    
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
        EMPTY_IN_TANK_TICK_ACTIONS.put(ItemBucket.class, new EmptyItemBucketInTankTickAction<TileSpiritReanimator>());
        EMPTY_IN_TANK_TICK_ACTIONS.put(IFluidContainerItem.class, new EmptyFluidContainerInTankTickAction<TileSpiritReanimator>());
    }

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
        addSlotsToSide(ForgeDirection.EAST, inSlotsTank);
        addSlotsToSide(ForgeDirection.UP, inSlots);
        addSlotsToSide(ForgeDirection.DOWN, outSlots);
        addSlotsToSide(ForgeDirection.SOUTH, outSlots);
        addSlotsToSide(ForgeDirection.WEST, outSlots);
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
     * Get the entity id that is contained in a box.
     * @return The entity or null if no box or invalid box.
     */
    public int getEntityID() {
    	ItemStack boxStack = getInventory().getStackInSlot(getConsumeSlot());
    	if(boxStack != null && boxStack.getItem() == getAllowedCookItem()) {
    		return BoxOfEternalClosure.getInstance().getSpiritID(boxStack);
    	}
    	return -1;
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
        return false;
    }

	@Override
	public boolean canWork() {
		ItemStack eggStack = getStackInSlot(SLOT_EGG);
		ItemStack outputStack = getStackInSlot(TileSpiritReanimator.SLOTS_OUTPUT);
		return eggStack != null /*&& ResurgenceEgg.getInstance().isEmpty(eggStack)*/
				&& getEntityID() != -1 && EntityList.entityEggs.get(getEntityID()) != null
				&& (outputStack == null || (outputStack.getMaxStackSize() > outputStack.stackSize)
					&& outputStack.getItemDamage() == getEntityID());
	}
	
	@Override
    public void onStateChanged() {
        sendUpdate();
    }

}
