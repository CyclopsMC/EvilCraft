package org.cyclops.evilcraft.tileentity.tickaction;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.tileentity.TankInventoryTileEntity;
import org.cyclops.evilcraft.core.tileentity.TickingTankInventoryTileEntity;
import org.cyclops.evilcraft.core.tileentity.tickaction.ITickAction;

/**
 * {@link ITickAction} for emptying fluid containers in a tank.
 * @author rubensworks
 *
 * @param <T> {@link TickingTankInventoryTileEntity} to drain to.
 */
public class EmptyFluidContainerInTankTickAction<T extends TickingTankInventoryTileEntity<T>> extends EmptyInTankTickAction<T> {

    @Override
    public void onTick(T tile, ItemStack itemStack, int slot, int tick) {
        ItemStack containerStack = tile.getInventory().getStackInSlot(slot);
        IFluidHandler container = FluidUtil.getFluidHandler(containerStack);
        if(container != null && FluidHelpers.hasFluid(container)) {
            ItemStack result;
            if (FluidUtil.tryEmptyContainer(containerStack, tile.getTank(), MB_PER_TICK, null, false).isSuccess()) {
                result = FluidUtil.tryEmptyContainer(containerStack.splitStack(1), tile.getTank(), MB_PER_TICK, null, true).getResult();
            } else {
                result = FluidUtil.tryEmptyContainer(containerStack.splitStack(1), tile.getTank(), Fluid.BUCKET_VOLUME, null, true).getResult();
            }
            if (result != null) {
                if (result.getCount() == 0) {
                    result = containerStack;
                    if (result.getCount() == 0) {
                        result = null;
                    }
                }
                tile.getInventory().setInventorySlotContents(slot, result);
            }
        }
    }
    
    @Override
    public float getRequiredTicks(T tile, int slot, int tick) {
        return getRequiredTicks(tile, tile.getInventory().getStackInSlot(slot));
    }
    
    /**
     * Get the required ticks for a given item.
     * @param tile The {@link TileEntity} to drain to.
     * @param itemStack The item to get the required ticks for.
     * @return The required ticks.
     */
    public static int getRequiredTicks(TankInventoryTileEntity tile, ItemStack itemStack) {
        IFluidHandler container = FluidUtil.getFluidHandler(itemStack);
        int amount = 0;
        if(FluidHelpers.hasFluid(container))
            amount = FluidHelpers.getAmount(FluidHelpers.getFluid(container));
        int capacity = Math.min(FluidHelpers.getCapacity(container), tile.getTank().getFluidAmount());
        return (capacity - amount) / MB_PER_TICK;
    }
    
    @Override
    public boolean canTick(T tile, ItemStack itemStack, int slot, int tick) {
        boolean emptyContainer = false;
        ItemStack containerStack = tile.getInventory().getStackInSlot(slot);
        IFluidHandler container = FluidUtil.getFluidHandler(containerStack);
        if(container != null && FluidHelpers.hasFluid(container)) {
            FluidStack fluidStack = FluidHelpers.getFluid(container);
            if(FluidHelpers.getAmount(fluidStack) <= 0)
                emptyContainer = true;
        } else emptyContainer = true;
        return super.canTick(tile, itemStack, slot, tick) && !emptyContainer;
    }

}
