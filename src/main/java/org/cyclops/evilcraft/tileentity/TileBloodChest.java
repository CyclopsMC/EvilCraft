package org.cyclops.evilcraft.tileentity;

import lombok.experimental.Delegate;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.IFluidContainerItem;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.cyclopscore.inventory.slot.SlotFluidContainer;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.block.BloodChest;
import org.cyclops.evilcraft.core.fluid.BloodFluidConverter;
import org.cyclops.evilcraft.core.fluid.ImplicitFluidConversionTank;
import org.cyclops.evilcraft.core.tileentity.TickingTankInventoryTileEntity;
import org.cyclops.evilcraft.core.tileentity.tickaction.ITickAction;
import org.cyclops.evilcraft.core.tileentity.tickaction.TickComponent;
import org.cyclops.evilcraft.fluid.Blood;
import org.cyclops.evilcraft.inventory.container.ContainerBloodChest;
import org.cyclops.evilcraft.inventory.slot.SlotRepairable;
import org.cyclops.evilcraft.tileentity.tickaction.EmptyFluidContainerInTankTickAction;
import org.cyclops.evilcraft.tileentity.tickaction.EmptyItemBucketInTankTickAction;
import org.cyclops.evilcraft.tileentity.tickaction.bloodchest.RepairItemTickAction;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    @Delegate
    private final ITickingTile tickingTileComponent = new TickingTileComponent(this);

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
        EMPTY_IN_TANK_TICK_ACTIONS.put(IFluidContainerItem.class, new EmptyFluidContainerInTankTickAction<TileBloodChest>());
        EMPTY_IN_TANK_TICK_ACTIONS.put(Item.class, new EmptyItemBucketInTankTickAction<TileBloodChest>());
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
                >(this, EMPTY_IN_TANK_TICK_ACTIONS, SLOT_CONTAINER, false)
                );
        
        // The slots side mapping
        List<Integer> inSlotsTank = new LinkedList<Integer>();
        inSlotsTank.add(SLOT_CONTAINER);
        List<Integer> inSlotsInventory = new LinkedList<Integer>();
        for(int i = 0; i < SLOTS_CHEST; i++) {
            inSlotsInventory.add(i);
        }
        inSlotsInventory.add(SLOT_CONTAINER);
        addSlotsToSide(EnumFacing.EAST, inSlotsTank);
        addSlotsToSide(EnumFacing.UP, inSlotsInventory);
        addSlotsToSide(EnumFacing.DOWN, inSlotsInventory);
        addSlotsToSide(EnumFacing.SOUTH, inSlotsInventory);
        addSlotsToSide(EnumFacing.WEST, inSlotsInventory);
    }

    @Override
    public EnumFacing getRotation() {
        return BlockHelpers.getSafeBlockStateProperty(getWorld().getBlockState(getPos()), BloodChest.FACING, EnumFacing.NORTH);
    }

    @Override
    public boolean canRenderBreaking() {
        return true;
    }
    
    @Override
    protected SingleUseTank newTank(String tankName, int tankSize) {
    	return new ImplicitFluidConversionTank(tankName, tankSize, this, BloodFluidConverter.getInstance());
    }
    
    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        if(slot == SLOT_CONTAINER)
            return SlotFluidContainer.checkIsItemValid(itemStack, getTank());
        else if(slot <= SLOTS_CHEST && slot >= 0)
            return SlotRepairable.checkIsItemValid(itemStack);
        return false;
    }

    @Override
    public int getNewState() {
        return 0;
    }

    @Override
    public void onStateChanged() {
        
    }
    
    @Override
    public void updateTileEntity() {
        super.updateTileEntity();
        int x = getPos().getX();
        int y = getPos().getY();
        int z = getPos().getZ();
        // Resynchronize clients with the server state, the last condition makes sure
        // not all chests are synced at the same time.
        if(worldObj != null
                && !this.worldObj.isRemote
                && this.playersUsing != 0
                && WorldHelpers.efficientTick(worldObj, TICK_MODULUS, this.getPos())/*(this.ticksSinceSync + this.xCoord + this.yCoord + this.zCoord) % 200 == 0*/) {
            this.playersUsing = 0;
            float range = 5.0F;

            @SuppressWarnings("unchecked")
            List<EntityPlayer> entities = this.worldObj.getEntitiesWithinAABB(
                    EntityPlayer.class,
                    new AxisAlignedBB(
                            (double)((float)x - range),
                            (double)((float)y - range),
                            (double)((float)z - range),
                            (double)((float)(x + 1) + range),
                            (double)((float)(y + 1) + range),
                            (double)((float)(z + 1) + range)
                            )
                    );

            for(EntityPlayer player : entities) {
                if (player.openContainer instanceof ContainerBloodChest) {
                    ++this.playersUsing;
                }
            }
            
            worldObj.addBlockEvent(getPos(), block, 1, playersUsing);
        }

        prevLidAngle = lidAngle;
        float increaseAngle = 0.1F;
        if (playersUsing > 0 && lidAngle == 0.0F) {
            EvilCraft.proxy.playSound(
                    (double) x + 0.5D,
                    (double) y + 0.5D,
                    (double) z + 0.5D,
                    SoundEvents.block_chest_open,
                    SoundCategory.BLOCKS,
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
                EvilCraft.proxy.playSound(
                        (double) x + 0.5D,
                        (double) y + 0.5D,
                        (double) z + 0.5D,
                        SoundEvents.block_chest_close,
                        SoundCategory.BLOCKS,
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
    public void openInventory(EntityPlayer entityPlayer) {
        triggerPlayerUsageChange(1);
    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer) {
        triggerPlayerUsageChange(-1);
    }
    
    private void triggerPlayerUsageChange(int change) {
        if (worldObj != null) {
            playersUsing += change;
            worldObj.addBlockEvent(getPos(), block, 1, playersUsing);
        }
    }
    
    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return super.isUseableByPlayer(entityPlayer)
                && (worldObj == null || worldObj.getTileEntity(getPos()) != this);
    }

}
