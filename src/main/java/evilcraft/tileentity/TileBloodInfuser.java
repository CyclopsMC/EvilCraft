package evilcraft.tileentity;

import com.google.common.collect.Sets;
import evilcraft.api.recipes.custom.IRecipe;
import evilcraft.block.BloodInfuser;
import evilcraft.core.fluid.BloodFluidConverter;
import evilcraft.core.fluid.ImplicitFluidConversionTank;
import evilcraft.core.fluid.SingleUseTank;
import evilcraft.core.inventory.slot.SlotFluidContainer;
import evilcraft.core.recipe.custom.DurationRecipeProperties;
import evilcraft.core.recipe.custom.ItemAndFluidStackRecipeComponent;
import evilcraft.core.recipe.custom.ItemStackRecipeComponent;
import evilcraft.core.tileentity.tickaction.ITickAction;
import evilcraft.core.tileentity.tickaction.TickComponent;
import evilcraft.fluid.Blood;
import evilcraft.tileentity.tickaction.EmptyFluidContainerInTankTickAction;
import evilcraft.tileentity.tickaction.EmptyItemBucketInTankTickAction;
import evilcraft.tileentity.tickaction.bloodinfuser.FluidContainerItemTickAction;
import evilcraft.tileentity.tickaction.bloodinfuser.InfuseItemTickAction;
import evilcraft.tileentity.tickaction.bloodinfuser.ItemBucketTickAction;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A machine that can infuse things with blood.
 * @author rubensworks
 *
 */
public class TileBloodInfuser extends TileWorking<TileBloodInfuser> {
    
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
                BloodInfuser.getInstance().getLocalizedName(),
                LIQUID_PER_SLOT,
                TileBloodInfuser.TANKNAME,
                ACCEPTED_FLUID,
                Sets.newHashSet(UPGRADE_EFFICIENCY, UPGRADE_SPEED, UPGRADE_TIER1, UPGRADE_TIER2, UPGRADE_TIER3));
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
    protected SingleUseTank newTank(String tankName, int tankSize) {
    	return new ImplicitFluidConversionTank(tankName, tankSize, this, BloodFluidConverter.getInstance());
    }
    
    @Override
    public boolean canConsume(ItemStack itemStack) {
        // Empty bucket
        if(itemStack.getItem() == Items.bucket
                && this.getTank().getFluidAmount() >= FluidContainerRegistry.BUCKET_VOLUME)
            return true;
        
        // Valid fluid container
        if(itemStack.getItem() instanceof IFluidContainerItem) {
            IFluidContainerItem container = (IFluidContainerItem) itemStack.getItem();
            FluidStack fluidStack = container.getFluid(itemStack);
            if(fluidStack == null) {
                return true;
            } else {
                if(getTank().canTankAccept(fluidStack.getFluid())
                        && fluidStack.amount < container.getCapacity(itemStack)) {
                    return true;
                }
            }
        }
        
        // Valid custom recipe
        ItemAndFluidStackRecipeComponent recipeInput = new ItemAndFluidStackRecipeComponent(itemStack, getTank().getFluid());
        IRecipe<ItemAndFluidStackRecipeComponent, ItemStackRecipeComponent, DurationRecipeProperties> recipe = 
        		BloodInfuser.getInstance().getRecipeRegistry().findRecipeByInput(recipeInput);
        if(recipe != null)
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
            return SlotFluidContainer.checkIsItemValid(itemStack, getTank());
        return false;
    }

    @Override
    public void onStateChanged() {
        sendUpdate();
    }

	@Override
	public boolean canWork() {
		return true;
	}

	@Override
	protected int getWorkTicker() {
		return infuseTicker;
	}

}
