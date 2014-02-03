package evilcraft.entities.tileentities.tickaction;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import evilcraft.api.entities.tileentitites.TankInventoryTileEntity;
import evilcraft.api.entities.tileentitites.TickingTankInventoryTileEntity;

public class EmptyFluidContainerInTankTickAction<T extends TickingTankInventoryTileEntity<T>> extends EmptyInTankTickAction<T> {

    @Override
    public void onTick(T tile, ItemStack itemStack, int slot, int tick) {
        ItemStack containerStack = tile.getInventory().getStackInSlot(slot);
        IFluidContainerItem container = (IFluidContainerItem) containerStack.getItem();
        if(container.getFluid(containerStack) != null) {
            FluidStack fluidStack = container.getFluid(containerStack).copy();
            fluidStack.amount = Math.min(MB_PER_TICK, fluidStack.amount);
            int filled = tile.getTank().fill(fluidStack, true);
            container.drain(containerStack, filled, true);
        }
    }
    
    @Override
    public int getRequiredTicks(T tile, int slot) {
        return getRequiredTicks(tile, tile.getInventory().getStackInSlot(slot));
    }
    
    public static int getRequiredTicks(TankInventoryTileEntity tile, ItemStack itemStack) {
        IFluidContainerItem container = (IFluidContainerItem) itemStack.getItem();
        int amount = 0;
        if(container.getFluid(itemStack) != null)
            amount = container.getFluid(itemStack).amount;
        int capacity = Math.min(container.getCapacity(itemStack), tile.getTank().getFluidAmount());
        return (capacity - amount) / MB_PER_TICK;
    }
    
    @Override
    public boolean canTick(T tile, ItemStack itemStack, int slot, int tick) {
        boolean emptyContainer = false;
        ItemStack containerStack = tile.getInventory().getStackInSlot(slot);
        IFluidContainerItem container = (IFluidContainerItem) containerStack.getItem();
        if(container.getFluid(containerStack) != null) {
            FluidStack fluidStack = container.getFluid(containerStack);
            if(fluidStack.amount <= 0)
                emptyContainer = true;
        } else emptyContainer = true;
        return super.canTick(tile, itemStack, slot, tick) && !emptyContainer;
    }

}
