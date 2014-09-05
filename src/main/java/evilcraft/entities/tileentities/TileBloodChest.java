package evilcraft.entities.tileentities;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.IFluidContainerItem;
import evilcraft.blocks.BloodChest;
import evilcraft.core.entities.tileentitites.TickingTankInventoryTileEntity;
import evilcraft.core.entities.tileentitites.tickaction.ITickAction;
import evilcraft.core.entities.tileentitites.tickaction.TickComponent;
import evilcraft.core.fluids.BloodFluidConverter;
import evilcraft.core.fluids.ImplicitFluidConversionTank;
import evilcraft.core.fluids.SingleUseTank;
import evilcraft.core.helpers.WorldHelpers;
import evilcraft.entities.tileentities.tickaction.EmptyFluidContainerInTankTickAction;
import evilcraft.entities.tileentities.tickaction.EmptyItemBucketInTankTickAction;
import evilcraft.entities.tileentities.tickaction.bloodchest.RepairItemTickAction;
import evilcraft.fluids.Blood;
import evilcraft.gui.container.ContainerBloodChest;
import evilcraft.gui.slot.SlotRepairable;

/**
 * A chest that is able to repair tools with the use of blood.
 * Partially based on cpw's IronChests.
 * @author rubensworks
 *
 */
public class TileBloodChest extends TickingTankInventoryTileEntity<TileBloodChest> {
	
	private static final int TICK_MODULUS = 200;
    
    /**
     * The amount of slots in the chest.
     */
    public static final int SLOTS_CHEST = ContainerBloodChest.CHEST_INVENTORY_ROWS * ContainerBloodChest.CHEST_INVENTORY_COLUMNS;
    /**
     * The total amount of slots.
     */
    public static final int SLOTS = SLOTS_CHEST + 1;
    /**
     * The id of the fluid container slot.
     */
    public static final int SLOT_CONTAINER = SLOTS - 1;
    
    /**
     * The name of the tank, used for NBT storage.
     */
    public static String TANKNAME = "bloodChestTank";
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
    /**
     * The previous angle of the lid.
     */
    public float prevLidAngle;
    /**
     * The current angle of the lid.
     */
    public float lidAngle;
    private int playersUsing;
    
    private Block block = BloodChest.getInstance();
    
    private static final Map<Class<?>, ITickAction<TileBloodChest>> REPAIR_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickAction<TileBloodChest>>();
    static {
        REPAIR_TICK_ACTIONS.put(Item.class, new RepairItemTickAction());
    }
    
    private static final Map<Class<?>, ITickAction<TileBloodChest>> EMPTY_IN_TANK_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickAction<TileBloodChest>>();
    static {
        EMPTY_IN_TANK_TICK_ACTIONS.put(ItemBucket.class, new EmptyItemBucketInTankTickAction<TileBloodChest>());
        EMPTY_IN_TANK_TICK_ACTIONS.put(IFluidContainerItem.class, new EmptyFluidContainerInTankTickAction<TileBloodChest>());
    }
    
    /**
     * Make a new instance.
     */
    public TileBloodChest() {
        super(
                SLOTS,
                BloodChest.getInstance().getLocalizedName(),
                LIQUID_PER_SLOT,
                TileBloodChest.TANKNAME,
                ACCEPTED_FLUID);
        for(int i = 0; i < SLOTS_CHEST; i++) {
            addTicker(
                    new TickComponent<
                        TileBloodChest,
                        ITickAction<TileBloodChest>
                    >(this, REPAIR_TICK_ACTIONS, i)
                    );
        }
        addTicker(
                new TickComponent<
                    TileBloodChest,
                    ITickAction<TileBloodChest>
                >(this, EMPTY_IN_TANK_TICK_ACTIONS, SLOT_CONTAINER)
                );
        
        // The slots side mapping
        List<Integer> inSlotsTank = new LinkedList<Integer>();
        inSlotsTank.add(SLOT_CONTAINER);
        List<Integer> inSlotsInventory = new LinkedList<Integer>();
        inSlotsInventory.add(SLOT_CONTAINER);
        addSlotsToSide(ForgeDirection.EAST, inSlotsTank);
        addSlotsToSide(ForgeDirection.UP, inSlotsInventory);
        addSlotsToSide(ForgeDirection.DOWN, inSlotsInventory);
        addSlotsToSide(ForgeDirection.SOUTH, inSlotsInventory);
        addSlotsToSide(ForgeDirection.WEST, inSlotsInventory);
    }
    
    @Override
    protected SingleUseTank newTank(String tankName, int tankSize) {
    	return new ImplicitFluidConversionTank(tankName, tankSize, this, BloodFluidConverter.getInstance());
    }
    
    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        return SlotRepairable.checkIsItemValid(itemStack);
    }

    @Override
    public int getNewState() {
        return 0;
    }

    @Override
    public void onStateChanged() {
        
    }
    
    @Override
    public void updateEntity() {
        super.updateEntity();
        // Resynchronize clients with the server state, the last condition makes sure
        // not all chests are synced at the same time.
        if(worldObj != null
                && !this.worldObj.isRemote
                && this.playersUsing != 0
                && WorldHelpers.efficientTick(worldObj, TICK_MODULUS, this.xCoord, this.yCoord, this.zCoord)/*(this.ticksSinceSync + this.xCoord + this.yCoord + this.zCoord) % 200 == 0*/) {
            this.playersUsing = 0;
            float range = 5.0F;
            @SuppressWarnings("unchecked")
            List<EntityPlayer> entities = this.worldObj.getEntitiesWithinAABB(
                    EntityPlayer.class,
                    AxisAlignedBB.getBoundingBox(
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
            
            worldObj.addBlockEvent(xCoord, yCoord, zCoord, block, 1, playersUsing);
        }

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
    public void openInventory() {
        triggerPlayerUsageChange(1);
    }

    @Override
    public void closeInventory() {
        triggerPlayerUsageChange(-1);
    }
    
    private void triggerPlayerUsageChange(int change) {
        if (worldObj != null) {
            playersUsing += change;
            worldObj.addBlockEvent(xCoord, yCoord, zCoord, block, 1, playersUsing);
        }
    }
    
    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return super.isUseableByPlayer(entityPlayer)
                && (worldObj == null || worldObj.getTileEntity(xCoord, yCoord, zCoord) != this);
    }

}
