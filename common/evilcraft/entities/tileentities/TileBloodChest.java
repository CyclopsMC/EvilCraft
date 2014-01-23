package evilcraft.entities.tileentities;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import evilcraft.api.entities.tileentitites.TickingTankInventoryTileEntity;
import evilcraft.blocks.BloodChestConfig;
import evilcraft.fluids.Blood;
import evilcraft.gui.container.ContainerBloodChest;
import evilcraft.gui.slot.SlotRepairable;

/**
 * Partially based on cpw's IronChests
 * @author rubensworks
 *
 */
public class TileBloodChest extends TickingTankInventoryTileEntity {
    
    public static final int SLOTS = ContainerBloodChest.CHEST_INVENTORY_ROWS * ContainerBloodChest.CHEST_INVENTORY_COLUMNS;
    
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
    
    private int ticksSinceSync = -1;
    public float prevLidAngle;
    public float lidAngle;
    private int playersUsing;
    
    private int repairTicker;
    
    private int blockID = BloodChestConfig._instance.ID;
    
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
        return SlotRepairable.checkIsItemValid(itemStack);
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
    
    @Override
    public void updateEntity()
    {
        super.updateEntity();
        // Resynchronize clients with the server state, the last condition makes sure
        // not all chests are synced at the same time.
        if(worldObj != null
                && !this.worldObj.isRemote
                && this.playersUsing != 0
                && (this.ticksSinceSync + this.xCoord + this.yCoord + this.zCoord) % 200 == 0) {
            this.playersUsing = 0;
            float range = 5.0F;
            @SuppressWarnings("unchecked")
            List<EntityPlayer> entities = this.worldObj.getEntitiesWithinAABB(
                    EntityPlayer.class,
                    AxisAlignedBB.getAABBPool().getAABB(
                            (double)((float)this.xCoord - range),
                            (double)((float)this.yCoord - range),
                            (double)((float)this.zCoord - range),
                            (double)((float)(this.xCoord + 1) + range),
                            (double)((float)(this.yCoord + 1) + range),
                            (double)((float)(this.zCoord + 1) + range)
                            )
                    );

            for(EntityPlayer player : entities) {
                if (player.openContainer instanceof ContainerBloodChest) {
                    ++this.playersUsing;
                }
            }
        }

        if (worldObj != null && !worldObj.isRemote && ticksSinceSync < 0) {
            worldObj.addBlockEvent(xCoord, yCoord, zCoord, blockID, 1, playersUsing);
        }

        this.ticksSinceSync++;
        prevLidAngle = lidAngle;
        float increaseAngle = 0.1F;
        if (playersUsing > 0 && lidAngle == 0.0F) {
            worldObj.playSoundEffect(
                    (double) xCoord + 0.5D,
                    (double) yCoord + 0.5D,
                    (double) zCoord + 0.5D,
                    "random.chestopen",
                    0.5F,
                    worldObj.rand.nextFloat() * 0.1F + 0.9F
                    );
        }
        if (playersUsing == 0 && lidAngle > 0.0F || playersUsing > 0 && lidAngle < 1.0F) {
            float preIncreaseAngle = lidAngle;
            if (playersUsing > 0) {
                lidAngle += increaseAngle;
            } else {
                lidAngle -= increaseAngle;
            }
            if (lidAngle > 1.0F) {
                lidAngle = 1.0F;
            }
            float closedAngle = 0.5F;
            if (lidAngle < closedAngle && preIncreaseAngle >= closedAngle) {
                worldObj.playSoundEffect(
                        (double) xCoord + 0.5D,
                        (double) yCoord + 0.5D,
                        (double) zCoord + 0.5D,
                        "random.chestclosed",
                        0.5F,
                        worldObj.rand.nextFloat() * 0.1F + 0.9F
                        );
            }
            if (lidAngle < 0.0F) {
                lidAngle = 0.0F;
            }
        }
    }

    @Override
    public boolean receiveClientEvent(int i, int j) {
        if (i == 1) {
            playersUsing = j;
        }
        return true;
    }

    @Override
    public void openChest() {
        triggerPlayerUsageChange(1);
    }

    @Override
    public void closeChest() {
        triggerPlayerUsageChange(-1);
    }
    
    private void triggerPlayerUsageChange(int change) {
        if (worldObj != null) {
            playersUsing += change;
            worldObj.addBlockEvent(xCoord, yCoord, zCoord, blockID, 1, playersUsing);
        }
    }
    
    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return super.isUseableByPlayer(entityPlayer)
                && (worldObj == null || worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this);
    }

}
