package evilcraft.entities.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
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
import net.minecraftforge.fluids.ItemFluidContainer;
import evilcraft.CustomRecipe;
import evilcraft.CustomRecipeRegistry;
import evilcraft.CustomRecipeResult;
import evilcraft.api.fluids.SingleUseTank;
import evilcraft.blocks.BloodInfuser;
import evilcraft.fluids.Blood;
import evilcraft.inventory.SimpleInventory;
import evilcraft.items.BucketBlood;

public class TileBloodInfuser extends ExtendedTileEntity implements IInventory, IFluidHandler{
    
    public static final int SLOT_CONTAINER = 0;
    public static final int SLOT_INFUSE = 1;
    public static final int LIQUID_PER_SLOT = FluidContainerRegistry.BUCKET_VOLUME * 10;
    public static final int TICKS_PER_MLIQUID = 2;
    
    protected SimpleInventory inventory = new SimpleInventory(2 , "BloodInfuser", 64);
    public SingleUseTank tank = new SingleUseTank("bloodTank", LIQUID_PER_SLOT, this);
    
    private int infuseTick = 0;
    
    public TileBloodInfuser() {
        tank.setAcceptedFluid(Blood.getInstance()); // Only accept blood!
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
        /*if (CoreProxy.proxy.isRenderWorld(worldObj)) {
            return;
        }

        balanceSlots();

        if (craftSlot == null) {
            internalPlayer = new InternalPlayer();
            craftSlot = new SlotCrafting(internalPlayer, craftMatrix, craftResult, 0, 0, 0);
        }
        if (resultInv.getStackInSlot(SLOT_RESULT) != null) {
            return;
        }
        update++;
        if (update % UPDATE_TIME == 0) {
            updateCrafting();
        }*/
    }
    
    private void fillTank() {
        // TODO
        if(!tank.isFull()) {
            ItemStack containerStack = inventory.getStackInSlot(SLOT_CONTAINER);
            if(containerStack != null && (containerStack.getItem() instanceof ItemBucket
                    || containerStack.getItem() instanceof IFluidContainerItem)) {
                // Two cases for fluid draining into the tank
                if(containerStack.getItem() instanceof ItemBucket) { // item is ItemBucket
                    FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(containerStack);
                    if(tank.getCapacity() - tank.getFluidAmount() >= FluidContainerRegistry.BUCKET_VOLUME
                            && fluidStack != null && tank.getAcceptedFluid().equals(fluidStack.getFluid())) {
                        tank.fill(fluidStack, true);
                        inventory.setInventorySlotContents(SLOT_CONTAINER, FluidContainerRegistry.EMPTY_BUCKET.copy());
                    }
                } else { // item is IFluidContainerItem
                    ItemFluidContainer container = (ItemFluidContainer) containerStack.getItem();
                    // In the following case our container is empty
                    if(container.getFluid(containerStack) != null) {
                        FluidStack fluidStack = container.getFluid(containerStack).copy();
                        int filled = tank.fill(fluidStack, true);
                        container.drain(containerStack, filled, true);
                    }
                }
            }
        }
    }
    
    private void infuse() {
        infuseTick++;
        if(!tank.isEmpty()) {
            ItemStack infuseStack = inventory.getStackInSlot(SLOT_INFUSE);
            if(infuseStack != null && infuseStack.getItem() != null) {
                if(infuseStack.getItem() instanceof ItemBucket) {
                    FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(infuseStack);
                    if(tank.getFluidAmount() >= FluidContainerRegistry.BUCKET_VOLUME && fluidStack == null) {
                        tank.drain(FluidContainerRegistry.BUCKET_VOLUME, true);
                        inventory.setInventorySlotContents(SLOT_INFUSE, new ItemStack(BucketBlood.getInstance()));
                    }
                } else if(infuseStack.getItem() instanceof IFluidContainerItem) {
                    ItemFluidContainer container = (ItemFluidContainer) infuseStack.getItem();
                    int filled = container.fill(infuseStack, tank.getFluid(), true);
                    tank.drain(filled, true);
                } else {
                    // TODO: recipe manager for blood infusings
                    CustomRecipe customRecipeKey = new CustomRecipe(infuseStack, tank.getFluid(), BloodInfuser.getInstance());
                    CustomRecipeResult result = CustomRecipeRegistry.get(customRecipeKey);
                    System.out.println("found:"+result);
                    if(result != null) {
                        inventory.setInventorySlotContents(SLOT_INFUSE, result.getResult().copy());
                        if(result.getRecipe().getFluidStack() != null) {
                            tank.drain(result.getRecipe().getFluidStack().amount, true);
                        }
                    }
                }
            }
        }
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return tank.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource,
            boolean doDrain) {
        if (resource == null || !resource.isFluidEqual(tank.getFluid()))
            return null;
        return drain(from, resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return tank.drain(maxDrain, doDrain);
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

}
