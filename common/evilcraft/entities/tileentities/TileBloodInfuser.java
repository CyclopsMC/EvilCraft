package evilcraft.entities.tileentities;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidHandler;
import evilcraft.CustomRecipe;
import evilcraft.CustomRecipeRegistry;
import evilcraft.CustomRecipeResult;
import evilcraft.api.entities.tileentitites.ExtendedTileEntity;
import evilcraft.api.fluids.SingleUseTank;
import evilcraft.blocks.BloodInfuser;
import evilcraft.entities.tileentities.tickaction.bloodinfuser.EmptyFluidContainerInTankTickAction;
import evilcraft.entities.tileentities.tickaction.bloodinfuser.EmptyItemBucketInTankTickAction;
import evilcraft.entities.tileentities.tickaction.bloodinfuser.FluidContainerItemTickAction;
import evilcraft.entities.tileentities.tickaction.bloodinfuser.ITickActionWithTank;
import evilcraft.entities.tileentities.tickaction.bloodinfuser.InfuseItemTickAction;
import evilcraft.entities.tileentities.tickaction.bloodinfuser.ItemBucketTickAction;
import evilcraft.fluids.Blood;
import evilcraft.inventory.SimpleInventory;

public class TileBloodInfuser extends ExtendedTileEntity implements IInventory, IFluidHandler, IConsumeProduceEmptyInTankTile {
    
    public static final int SLOTS = 3;
    public static final int SLOT_CONTAINER = 0;
    public static final int SLOT_INFUSE = 1;
    public static final int SLOT_INFUSE_RESULT = 2;
    public static final int LIQUID_PER_SLOT = FluidContainerRegistry.BUCKET_VOLUME * 10;
    public static final int TICKS_PER_MLIQUID = 2;
    public static final Fluid ACCEPTED_FLUID = Blood.getInstance();
    
    public static final Map<Class<?>, ITickActionWithTank<IConsumeProduceEmptyInTankTile>> INFUSE_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickActionWithTank<IConsumeProduceEmptyInTankTile>>();
    static {
        INFUSE_TICK_ACTIONS.put(ItemBucket.class, new ItemBucketTickAction());
        INFUSE_TICK_ACTIONS.put(IFluidContainerItem.class, new FluidContainerItemTickAction());
        INFUSE_TICK_ACTIONS.put(Item.class, new InfuseItemTickAction());
    }
    
    public static final Map<Class<?>, ITickActionWithTank<IConsumeProduceEmptyInTankTile>> EMPTY_IN_TANK_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickActionWithTank<IConsumeProduceEmptyInTankTile>>();
    static {
        EMPTY_IN_TANK_TICK_ACTIONS.put(ItemBucket.class, new EmptyItemBucketInTankTickAction());
        EMPTY_IN_TANK_TICK_ACTIONS.put(IFluidContainerItem.class, new EmptyFluidContainerInTankTickAction());
    }
    
    protected SimpleInventory inventory = new SimpleInventory(SLOTS , "BloodInfuser", 64);
    public SingleUseTank tank = new SingleUseTank("bloodTank", LIQUID_PER_SLOT, this);
    
    public int infuseTick = 0;
    public int requiredTicks = 0;
    public int emptyTick = 0;
    
    public TileBloodInfuser() {
        tank.setAcceptedFluid(ACCEPTED_FLUID); // Only accept blood!
    }
    
    public int getInventorySize() {
        return inventory.getSizeInventory();
    }
    
    public static ITickActionWithTank<IConsumeProduceEmptyInTankTile> getTickAction(Item item, Map<Class<?>, ITickActionWithTank<IConsumeProduceEmptyInTankTile>> map) {
        for(Entry<Class<?>, ITickActionWithTank<IConsumeProduceEmptyInTankTile>> entry : map.entrySet()) {
            if(entry.getKey().isInstance(item)) {
                return entry.getValue();
            }
        }
        return null;
    }
    
    @Override
    public SingleUseTank getTank() {
        return tank;
    }

    @Override
    public int getSizeInventory() {
        return inventory.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int slotId) {
        if(slotId >= getSizeInventory() || slotId < 0)
            return null;
        return inventory.getStackInSlot(slotId);
    }

    @Override
    public ItemStack decrStackSize(int slotId, int count) {
        return inventory.decrStackSize(slotId, count);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slotId) {
        return inventory.getStackInSlotOnClosing(slotId);
    }

    @Override
    public void setInventorySlotContents(int slotId, ItemStack itemstack) {
        inventory.setInventorySlotContents(slotId, itemstack);
    }

    @Override
    public String getInvName() {
        return inventory.getInvName();
    }

    @Override
    public boolean isInvNameLocalized() {
        return inventory.isInvNameLocalized();
    }

    @Override
    public int getInventoryStackLimit() {
        return inventory.getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this && entityPlayer.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64.0D;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        inventory.readFromNBT(data);
        tank.readFromNBT(data);
    }

    @Override
    public void writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        inventory.writeToNBT(data);
        tank.writeToNBT(data);
    }

    @Override
    public void openChest() {
        
    }

    @Override
    public void closeChest() {
        
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        if (itemstack == null)
            return false;
        if (!itemstack.isStackable())
            return false;
        if (itemstack.getItem().hasContainerItem())
            return false;
        if (getStackInSlot(slot) == null)
            return false;
        
        return true;
    }
    
    @Override
    public boolean canUpdate() {
        return true;
    }
    
    @Override
    public void updateEntity() {
        super.updateEntity();
        fillTank();
        infuse();
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
    
    private void fillTank() {
        emptyTick = tick(emptyTick, inventory.getStackInSlot(SLOT_CONTAINER), EMPTY_IN_TANK_TICK_ACTIONS, false);
    }
    
    private void infuse() {
        infuseTick = tick(infuseTick, inventory.getStackInSlot(SLOT_INFUSE), INFUSE_TICK_ACTIONS, true);
    }
    
    private int tick(int tick, ItemStack itemStack, Map<Class<?>, ITickActionWithTank<IConsumeProduceEmptyInTankTile>> map, boolean setRequiredTicks) {
        if(itemStack != null) {
            ITickActionWithTank<IConsumeProduceEmptyInTankTile> action = getTickAction(itemStack.getItem(), map);
            if(action.canTick(this, tick)){
                if(tick == 0 && setRequiredTicks)
                    requiredTicks = action.getRequiredTicks(this);
                tick++;
                action.onTick(this, tick);
            } else {
                tick = 0;
            }
        } else tick = 0;
        return tick;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        int filled = tank.fill(resource, doFill);
        return filled;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource,
            boolean doDrain) {
        if (resource == null || !resource.isFluidEqual(tank.getFluid()))
            return null;
        FluidStack drained = drain(from, resource.amount, doDrain);
        return drained;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        FluidStack drained = tank.drain(maxDrain, doDrain);
        return drained;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return tank.getAcceptedFluid().equals(fluid);
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return tank.getAcceptedFluid().equals(fluid);
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        FluidTankInfo[] info = new FluidTankInfo[1];
        info[0] = tank.getInfo();
        return info;
    }

    @Override
    public SimpleInventory getInventory() {
        return inventory;
    }

    @Override
    public int getConsumeSlot() {
        return SLOT_INFUSE;
    }

    @Override
    public int getProduceSlot() {
        return SLOT_INFUSE_RESULT;
    }
    
    public boolean isInfusing() {
        return infuseTick > 0;
    }

    public int getInfuseTickScaled(int scale) {
        return (int) ((float)infuseTick / (float)requiredTicks * (float)scale);
    }

    @Override
    public int getEmptyToTankSlot() {
        return SLOT_CONTAINER;
    }

}
