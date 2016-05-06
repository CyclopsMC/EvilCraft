package org.cyclops.evilcraft.tileentity;

import lombok.experimental.Delegate;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.*;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.cyclopscore.tileentity.InventoryTileEntity;

import java.util.Collections;

/**
 * A block that can display items.
 * @author rubensworks
 *
 */
public class TileDisplayStand extends InventoryTileEntity implements CyclopsTileEntity.ITickingTile, IFluidHandler {

    @Delegate
    protected final ITickingTile tickingTileComponent = new TickingTileComponent(this);

    @NBTPersist
    private ItemStack displayStandType;
    @NBTPersist
    private boolean directionPositive = false;

    public TileDisplayStand() {
        super(1, "displayStand", 1);
        for (EnumFacing side : EnumFacing.values()) {
            addSlotsToSide(side, Collections.singleton(0));
        }
    }

    public EnumFacing.AxisDirection getDirection() {
        return directionPositive ? EnumFacing.AxisDirection.POSITIVE : EnumFacing.AxisDirection.NEGATIVE;
    }

    public void setDirection(EnumFacing.AxisDirection direction) {
        this.directionPositive = direction == EnumFacing.AxisDirection.POSITIVE;
        sendUpdate();
    }

    public ItemStack getDisplayStandType() {
        return this.displayStandType;
    }

    protected FluidStack getFluidStack() {
        ItemStack itemStack = getStackInSlot(0);
        if (itemStack != null && itemStack.getItem() instanceof IFluidContainerItem) {
            return ((IFluidContainerItem) itemStack.getItem()).getFluid(itemStack);
        }
        return null;
    }

    protected int getFluidCapacity() {
        ItemStack itemStack = getStackInSlot(0);
        if (itemStack != null && itemStack.getItem() instanceof IFluidContainerItem) {
            return ((IFluidContainerItem) itemStack.getItem()).getCapacity(itemStack);
        }
        return 0;
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        ItemStack itemStack = getStackInSlot(0);
        if (itemStack != null && itemStack.getItem() instanceof IFluidContainerItem) {
            return ((IFluidContainerItem) itemStack.getItem()).fill(itemStack, resource, doFill);
        }
        return 0;
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        FluidStack fluidStack = getFluidStack();
        if (resource != null && fluidStack != null && resource.getFluid() == fluidStack.getFluid()) {
            return drain(from, resource, doDrain);
        }
        return null;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        ItemStack itemStack = getStackInSlot(0);
        if (itemStack.getItem() instanceof IFluidContainerItem) {
            return ((IFluidContainerItem) itemStack.getItem()).drain(itemStack, maxDrain, doDrain);
        }
        return null;
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        ItemStack itemStack = getStackInSlot(0);
        return itemStack != null && itemStack.getItem() instanceof IFluidContainerItem;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        ItemStack itemStack = getStackInSlot(0);
        return itemStack != null && itemStack.getItem() instanceof IFluidContainerItem;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        return new FluidTankInfo[] {
                new FluidTankInfo(getFluidStack(), getFluidCapacity())
        };
    }

    @Override
    public void setInventorySlotContents(int slotId, ItemStack itemstack) {
        super.setInventorySlotContents(slotId, itemstack);
        sendUpdate();
    }
}
