package evilcraft.entities.tileentities;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import evilcraft.api.entities.tileentitites.TickingTankInventoryTileEntity;
import evilcraft.api.entities.tileentitites.tickaction.ITickAction;
import evilcraft.api.entities.tileentitites.tickaction.TickComponent;
import evilcraft.api.recipes.CustomRecipe;
import evilcraft.api.recipes.CustomRecipeRegistry;
import evilcraft.api.recipes.CustomRecipeResult;
import evilcraft.blocks.BloodInfuser;
import evilcraft.blocks.BloodInfuserConfig;
import evilcraft.entities.tileentities.tickaction.EmptyFluidContainerInTankTickAction;
import evilcraft.entities.tileentities.tickaction.EmptyItemBucketInTankTickAction;
import evilcraft.entities.tileentities.tickaction.bloodinfuser.FluidContainerItemTickAction;
import evilcraft.entities.tileentities.tickaction.bloodinfuser.InfuseItemTickAction;
import evilcraft.entities.tileentities.tickaction.bloodinfuser.ItemBucketTickAction;
import evilcraft.fluids.Blood;
import evilcraft.gui.slot.SlotFluidContainer;

/**
 * A machine that can infuse things with blood.
 * @author rubensworks
 *
 */
public class TileBloodInfuser extends TickingTankInventoryTileEntity<TileBloodInfuser> {
    
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
     * The name of the tank, used for NBT storage.
     */
    public static String TANKNAME = "bloodInfuserTank";
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
    
    private int infuseTicker;
    
    private static final Map<Class<?>, ITickAction<TileBloodInfuser>> INFUSE_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickAction<TileBloodInfuser>>();
    static {
        INFUSE_TICK_ACTIONS.put(ItemBucket.class, new ItemBucketTickAction());
        INFUSE_TICK_ACTIONS.put(IFluidContainerItem.class, new FluidContainerItemTickAction());
        INFUSE_TICK_ACTIONS.put(Item.class, new InfuseItemTickAction());
    }
    
    private static final Map<Class<?>, ITickAction<TileBloodInfuser>> EMPTY_IN_TANK_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickAction<TileBloodInfuser>>();
    static {
        EMPTY_IN_TANK_TICK_ACTIONS.put(ItemBucket.class, new EmptyItemBucketInTankTickAction<TileBloodInfuser>());
        EMPTY_IN_TANK_TICK_ACTIONS.put(IFluidContainerItem.class, new EmptyFluidContainerInTankTickAction<TileBloodInfuser>());
    }
    
    /**
     * Make a new instance.
     */
    public TileBloodInfuser() {
        super(
                SLOTS,
                BloodInfuserConfig._instance.NAME,
                LIQUID_PER_SLOT,
                TileBloodInfuser.TANKNAME,
                ACCEPTED_FLUID);
        infuseTicker = addTicker(
                new TickComponent<
                    TileBloodInfuser,
                    ITickAction<TileBloodInfuser>
                >(this, INFUSE_TICK_ACTIONS, SLOT_INFUSE)
                );
        addTicker(
                new TickComponent<
                    TileBloodInfuser,
                    ITickAction<TileBloodInfuser>
                >(this, EMPTY_IN_TANK_TICK_ACTIONS, SLOT_CONTAINER)
                );
        
        // The slots side mapping
        List<Integer> inSlots = new LinkedList<Integer>();
        inSlots.add(SLOT_INFUSE);
        List<Integer> inSlotsTank = new LinkedList<Integer>();
        inSlotsTank.add(SLOT_CONTAINER);
        List<Integer> outSlots = new LinkedList<Integer>();
        outSlots.add(SLOT_INFUSE_RESULT);
        addSlotsToSide(ForgeDirection.EAST, inSlotsTank);
        addSlotsToSide(ForgeDirection.UP, inSlots);
        addSlotsToSide(ForgeDirection.DOWN, outSlots);
        addSlotsToSide(ForgeDirection.SOUTH, outSlots);
        addSlotsToSide(ForgeDirection.WEST, outSlots);
    }
    
    /**
     * Check if the given item can be infused.
     * @param itemStack The item to check.
     * @return If it can be infused.
     */
    public boolean canConsume(ItemStack itemStack) {
        // Empty bucket
        if(itemStack.getItem().itemID == Item.bucketEmpty.itemID
                && this.getTank().getFluidAmount() >= FluidContainerRegistry.BUCKET_VOLUME)
            return true;
        
        // Valid fluid container
        if(itemStack.getItem() instanceof IFluidContainerItem) {
            IFluidContainerItem container = (IFluidContainerItem) itemStack.getItem();
            FluidStack fluidStack = container.getFluid(itemStack);
            if(fluidStack == null) {
                return true;
            } else {
                if(fluidStack.getFluid() == tank.getAcceptedFluid()
                        && fluidStack.amount < container.getCapacity(itemStack)) {
                    return true;
                }
            }
        }
        
        // Valid custom recipe
        CustomRecipe customRecipeKey = new CustomRecipe(itemStack, tank.getFluid(), BloodInfuser.getInstance());
        CustomRecipeResult result = CustomRecipeRegistry.get(customRecipeKey);
        if(result != null)
            return true;
        
        // In all other cases: false
        return false;
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
    
    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        if(slot == SLOT_INFUSE)
            return canConsume(itemStack);
        if(slot == SLOT_CONTAINER)
            return SlotFluidContainer.checkIsItemValid(itemStack, ACCEPTED_FLUID);
        return false;
    }
    
    /**
     * If the blood infuser is running an infusion.
     * @return If it is infusing.
     */
    public boolean isInfusing() {
        return getInfuseTick() > 0;
    }
    
    /**
     * If the blood infuser should visually (block icon) show it is infusing, should only be called client-side.
     * @return If the state is infusing.
     */
    public boolean isBlockInfusing() {
        return getCurrentState() == 1;
    }

    /**
     * Get the infuse progress scaled, to be used in GUI's.
     * @param scale The scale this progress should be applied to.
     * @return The scaled infusion progress.
     */
    public int getInfuseTickScaled(int scale) {
        return (int) ((float)getInfuseTick() / (float)getRequiredTicks() * (float)scale);
    }
    
    private int getInfuseTick() {
        return getTickers().get(infuseTicker).getTick();
    }
    
    private int getRequiredTicks() {
        return getTickers().get(infuseTicker).getRequiredTicks();
    }
    
    /**
     * Resets the ticks of the infusion.
     */
    public void resetInfusion() {
        getTickers().get(infuseTicker).setTick(0);
        getTickers().get(infuseTicker).setRequiredTicks(0);
    }

    @Override
    public int getNewState() {
        return this.isInfusing()?1:0;
    }

    @Override
    public void onStateChanged() {
        sendUpdate();
    }

}
