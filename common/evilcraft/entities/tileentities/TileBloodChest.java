package evilcraft.entities.tileentities;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import evilcraft.api.entities.tileentitites.TickingTankInventoryTileEntity;
import evilcraft.blocks.BloodChestConfig;
import evilcraft.fluids.Blood;

public class TileBloodChest extends TickingTankInventoryTileEntity {
    
    public static final int SLOTS = 10;
    
    public static String TANKNAME = "bloodChestTank";
    public static final int LIQUID_PER_SLOT = FluidContainerRegistry.BUCKET_VOLUME * 10;
    public static final int TICKS_PER_LIQUID = 2;
    public static final Fluid ACCEPTED_FLUID = Blood.getInstance();
    
    /*public static final Map<Class<?>, ITickAction<IConsumeProduceEmptyInTankTile>> INFUSE_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickAction<IConsumeProduceEmptyInTankTile>>();
    static {
        INFUSE_TICK_ACTIONS.put(ItemBucket.class, new ItemBucketTickAction());
        INFUSE_TICK_ACTIONS.put(IFluidContainerItem.class, new FluidContainerItemTickAction());
        INFUSE_TICK_ACTIONS.put(Item.class, new InfuseItemTickAction());
    }
    
    public static final Map<Class<?>, ITickAction<IConsumeProduceEmptyInTankTile>> EMPTY_IN_TANK_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickAction<IConsumeProduceEmptyInTankTile>>();
    static {
        EMPTY_IN_TANK_TICK_ACTIONS.put(ItemBucket.class, new EmptyItemBucketInTankTickAction());
        EMPTY_IN_TANK_TICK_ACTIONS.put(IFluidContainerItem.class, new EmptyFluidContainerInTankTickAction());
    }*/
    
    private int repairTicker;
    
    public TileBloodChest() {
        super(
                SLOTS,
                BloodChestConfig._instance.NAME,
                LIQUID_PER_SLOT,
                TileBloodChest.TANKNAME,
                ACCEPTED_FLUID);
        /*repairTicker = addTicker(
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
                );*/
        
        // The slots side mapping
        /*List<Integer> inSlots = new LinkedList<Integer>();
        inSlots.add(SLOT_INFUSE);
        List<Integer> inSlotsTank = new LinkedList<Integer>();
        inSlotsTank.add(SLOT_CONTAINER);
        List<Integer> outSlots = new LinkedList<Integer>();
        outSlots.add(SLOT_INFUSE_RESULT);
        addSlotsToSide(ForgeDirection.EAST, inSlotsTank);
        addSlotsToSide(ForgeDirection.UP, inSlots);
        addSlotsToSide(ForgeDirection.DOWN, outSlots);
        addSlotsToSide(ForgeDirection.SOUTH, outSlots);
        addSlotsToSide(ForgeDirection.WEST, outSlots);*/
    }
    
    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        return itemStack.isItemStackDamageable();
    }

    /*@Override
    public int getEmptyToTankSlot() {
        return SLOT_CONTAINER;
    }
    
    private int getInfuseTick() {
        return getTickers().get(infuseTicker).getTick();
    }
    
    private int getRequiredTicks() {
        return getTickers().get(infuseTicker).getRequiredTicks();
    }*/

    @Override
    public int getState() {
        return 0;
    }

    @Override
    public void onStateChanged() {
        //worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

}
