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
import evilcraft.CustomRecipe;
import evilcraft.CustomRecipeRegistry;
import evilcraft.CustomRecipeResult;
import evilcraft.api.entities.tileentitites.IConsumeProduceEmptyInTankTile;
import evilcraft.api.entities.tileentitites.TickingTankInventoryTileEntity;
import evilcraft.api.entities.tileentitites.tickaction.ITickAction;
import evilcraft.api.entities.tileentitites.tickaction.TickComponent;
import evilcraft.blocks.BloodInfuser;
import evilcraft.blocks.BloodInfuserConfig;
import evilcraft.entities.tileentities.tickaction.bloodinfuser.EmptyFluidContainerInTankTickAction;
import evilcraft.entities.tileentities.tickaction.bloodinfuser.EmptyItemBucketInTankTickAction;
import evilcraft.entities.tileentities.tickaction.bloodinfuser.FluidContainerItemTickAction;
import evilcraft.entities.tileentities.tickaction.bloodinfuser.InfuseItemTickAction;
import evilcraft.entities.tileentities.tickaction.bloodinfuser.ItemBucketTickAction;
import evilcraft.fluids.Blood;
import evilcraft.gui.slot.SlotFluidContainer;

public class TileBloodInfuser extends TickingTankInventoryTileEntity implements IConsumeProduceEmptyInTankTile {
    
    public static final int SLOTS = 3;
    public static final int SLOT_CONTAINER = 0;
    public static final int SLOT_INFUSE = 1;
    public static final int SLOT_INFUSE_RESULT = 2;
    
    public static final int LIQUID_PER_SLOT = FluidContainerRegistry.BUCKET_VOLUME * 10;
    public static final int TICKS_PER_LIQUID = 2;
    public static final Fluid ACCEPTED_FLUID = Blood.getInstance();
    
    public static final Map<Class<?>, ITickAction<IConsumeProduceEmptyInTankTile>> INFUSE_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickAction<IConsumeProduceEmptyInTankTile>>();
    static {
        INFUSE_TICK_ACTIONS.put(ItemBucket.class, new ItemBucketTickAction());
        INFUSE_TICK_ACTIONS.put(IFluidContainerItem.class, new FluidContainerItemTickAction());
        INFUSE_TICK_ACTIONS.put(Item.class, new InfuseItemTickAction());
    }
    
    public static final Map<Class<?>, ITickAction<IConsumeProduceEmptyInTankTile>> EMPTY_IN_TANK_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickAction<IConsumeProduceEmptyInTankTile>>();
    static {
        EMPTY_IN_TANK_TICK_ACTIONS.put(ItemBucket.class, new EmptyItemBucketInTankTickAction());
        EMPTY_IN_TANK_TICK_ACTIONS.put(IFluidContainerItem.class, new EmptyFluidContainerInTankTickAction());
    }
    
    private int infuseTicker;
    
    public TileBloodInfuser() {
        super(
                SLOTS,
                BloodInfuserConfig._instance.NAME,
                LIQUID_PER_SLOT,
                BloodInfuserConfig._instance.NAME + " Tank",
                ACCEPTED_FLUID);
        infuseTicker = addTicker(
                new TickComponent<
                    IConsumeProduceEmptyInTankTile,
                    ITickAction<IConsumeProduceEmptyInTankTile>
                >(this, INFUSE_TICK_ACTIONS, SLOT_INFUSE, true)
                );
        addTicker(
                new TickComponent<
                    IConsumeProduceEmptyInTankTile,
                    ITickAction<IConsumeProduceEmptyInTankTile>
                >(this, EMPTY_IN_TANK_TICK_ACTIONS, SLOT_CONTAINER, false)
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
    
    @Override
    public boolean canConsume(ItemStack itemStack) {
        // Empty bucket
        if(itemStack.getItem().itemID == Item.bucketEmpty.itemID
                && this.getTank().getFluidAmount() >= FluidContainerRegistry.BUCKET_VOLUME)
            return true;
        
        // Valid fluid container
        if(itemStack.getItem() instanceof IFluidContainerItem) {
            IFluidContainerItem container = (IFluidContainerItem) itemStack.getItem();
            FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(itemStack);
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
    
    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        if(slot == SLOT_INFUSE)
            return canConsume(itemStack);
        if(slot == SLOT_CONTAINER)
            return SlotFluidContainer.checkIsItemValid(itemStack);
        return false;
    }

    @Override
    public int getConsumeSlot() {
        return SLOT_INFUSE;
    }

    @Override
    public int getProduceSlot() {
        return SLOT_INFUSE_RESULT;
    }
    
    /**
     * If the blood infuser is running an infusion.
     * @return If it is infusing.
     */
    public boolean isInfusing() {
        return getInfuseTick() > 0;
    }

    /**
     * Get the infuse progress scaled, to be used in GUI's.
     * @param scale The scale this progress should be applied to.
     * @return The scaled infusion progress.
     */
    public int getInfuseTickScaled(int scale) {
        return (int) ((float)getInfuseTick() / (float)getRequiredTicks() * (float)scale);
    }

    @Override
    public int getEmptyToTankSlot() {
        return SLOT_CONTAINER;
    }
    
    private int getInfuseTick() {
        return getTickers().get(infuseTicker).getTick();
    }
    
    private int getRequiredTicks() {
        return getTickers().get(infuseTicker).getRequiredTicks();
    }

}
