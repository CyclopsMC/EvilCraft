package org.cyclops.evilcraft.tileentity.tickaction;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.evilcraft.core.tileentity.TickingTankInventoryTileEntity;
import org.cyclops.evilcraft.core.tileentity.tickaction.ITickAction;

/**
 * {@link ITickAction} that can empty buckets in tanks.
 * @author rubensworks
 *
 * @param <T> Extension of {@link TickingTankInventoryTileEntity} that has a tank.
 */
public class EmptyItemBucketInTankTickAction<T extends TickingTankInventoryTileEntity<T>> extends EmptyInTankTickAction<T> {

    @Override
    public void onTick(T tile, ItemStack itemStack, int slot, int tick) {
        if(tick >= getRequiredTicks(tile, slot, tick)) {
            FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(itemStack);
            SingleUseTank tank = tile.getTank();
            if(fluidStack != null && tank.canTankAccept(fluidStack.getFluid())
            		&& tank.canCompletelyFill(fluidStack)) {
                tank.fill(fluidStack, true);
                ItemStack drained = FluidContainerRegistry.drainFluidContainer(itemStack);
                if(drained.stackSize == 0) {
                    drained = itemStack.copy();
                    drained.stackSize--;
                    if(drained.stackSize == 0) drained = null;
                    tile.getInventory().setInventorySlotContents(slot, drained);
                } else {
                    tile.getInventory().setInventorySlotContents(slot, drained);
                }
            }
        }
    }
    
    @Override
    public boolean canTick(T tile, ItemStack itemStack, int slot, int tick) {
        ItemStack containerStack = tile.getInventory().getStackInSlot(slot);
        return super.canTick(tile, itemStack, slot, tick) && containerStack.getItem() != Items.bucket;
    }
    
    @Override
    public float getRequiredTicks(T tile, int slot, int tick) {
        return FluidContainerRegistry.BUCKET_VOLUME / MB_PER_TICK;
    }

}
