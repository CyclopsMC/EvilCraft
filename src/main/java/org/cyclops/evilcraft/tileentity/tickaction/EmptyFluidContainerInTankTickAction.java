package org.cyclops.evilcraft.tileentity.tickaction;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.ItemStackHelpers;
import org.cyclops.evilcraft.core.tileentity.TankInventoryTileEntity;
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
        ItemStack containerStack = tile.getInventory().getStackInSlot(slot).copy();
        IFluidHandler container = FluidUtil.getFluidHandler(containerStack).orElse(null);
        if(container != null && FluidHelpers.hasFluid(container)) {
            FluidActionResult result;
            if (FluidUtil.getFluidHandler(containerStack)
                    .map(h -> !h.drain(MB_PER_TICK, IFluidHandler.FluidAction.SIMULATE).isEmpty())
                    .orElse(false)) {
                result = FluidUtil.tryEmptyContainer(containerStack.split(1), tile.getTank(), MB_PER_TICK, null, true);
            } else {
                result = FluidUtil.tryEmptyContainer(containerStack.split(1), tile.getTank(), FluidHelpers.BUCKET_VOLUME, null, true);
            }
            if (result.isSuccess()) {
                ItemStack resultStack = result.getResult();
                if (resultStack.getCount() == 0) {
                    resultStack = containerStack;
                    if (resultStack.getCount() == 0) {
                        resultStack = ItemStack.EMPTY;
                    }
                } else {
                    if (containerStack.getCount() > 0) {
                        // In this case we have an "empty container", and a remaining container stack.
                        // Let's pop out the empty container in this case
                        ItemStackHelpers.spawnItemStack(tile.getWorld(), tile.getPos(), resultStack.copy());
                        resultStack = containerStack;
                        // TODO: in the next major update, rewrite this so that we have a proper "empty container" slot.
                    }
                }
                tile.getInventory().setInventorySlotContents(slot, resultStack);
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
        IFluidHandler container = FluidUtil.getFluidHandler(itemStack).orElse(null);
        int amount = 0;
        if(container != null && FluidHelpers.hasFluid(container))
            amount = FluidHelpers.getAmount(FluidHelpers.getFluid(container));
        int capacity = Math.min(FluidHelpers.getCapacity(container), tile.getTank().getFluidAmount());
        return (capacity - amount) / MB_PER_TICK;
    }
    
    @Override
    public boolean canTick(T tile, ItemStack itemStack, int slot, int tick) {
        boolean emptyContainer = false;
        ItemStack containerStack = tile.getInventory().getStackInSlot(slot);
        IFluidHandler container = FluidUtil.getFluidHandler(containerStack).orElse(null);
        if(container != null && FluidHelpers.hasFluid(container)) {
            FluidStack fluidStack = FluidHelpers.getFluid(container);
            if(FluidHelpers.getAmount(fluidStack) <= 0)
                emptyContainer = true;
        } else emptyContainer = true;
        return super.canTick(tile, itemStack, slot, tick) && !emptyContainer;
    }

}
