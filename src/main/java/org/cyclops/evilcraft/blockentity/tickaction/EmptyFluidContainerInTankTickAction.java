package org.cyclops.evilcraft.blockentity.tickaction;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;
import org.cyclops.cyclopscore.helper.IFluidHelpersNeoForge;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;
import org.cyclops.evilcraft.core.blockentity.BlockEntityTankInventory;
import org.cyclops.evilcraft.core.blockentity.BlockEntityTickingTankInventory;
import org.cyclops.evilcraft.core.blockentity.tickaction.ITickAction;

/**
 * {@link ITickAction} for emptying fluid containers in a tank.
 * @author rubensworks
 *
 * @param <T> {@link BlockEntityTickingTankInventory} to drain to.
 */
public class EmptyFluidContainerInTankTickAction<T extends BlockEntityTickingTankInventory<T>> extends EmptyInTankTickAction<T> {

    @Override
    public void onTick(T tile, ItemStack itemStack, int slot, int tick) {
        ItemStack containerStackOriginal = tile.getInventory().getItem(slot).copy();
        ItemAccess containerItemAccess = ItemAccess.forHandlerIndex(VanillaContainerWrapper.of(tile.getInventory()), slot).oneByOne();
        ResourceHandler<FluidResource> container = containerItemAccess.getCapability(Capabilities.Fluid.ITEM);
        IFluidHelpersNeoForge fh = IModHelpersNeoForge.get().getFluidHelpers();
        if(container != null && fh.hasFluid(container)) {
            FluidStack moved = fh.move(container, tile.getTank(), MB_PER_TICK, null, false, false);
            if (moved.isEmpty()) {
                moved = fh.move(container, tile.getTank(), IModHelpersNeoForge.get().getFluidHelpers().getBucketVolume(), null, false, false);
            }
            if (!moved.isEmpty()) {
                if (!containerItemAccess.getResource().is(containerStackOriginal.getItem())) {
                    // In this case we have an "empty container", and a remaining container stack.
                    // Let's pop out the empty container in this case
                    IModHelpers.get().getItemStackHelpers().spawnItemStack(tile.getLevel(), tile.getBlockPos(), containerItemAccess.getResource().toStack());
                    tile.getInventory().setItem(slot, ItemStack.EMPTY);
                }
            }
        }
    }

    @Override
    public float getRequiredTicks(T tile, int slot, int tick) {
        return getRequiredTicks(tile, tile.getInventory().getItem(slot));
    }

    /**
     * Get the required ticks for a given item.
     * @param tile The {@link BlockEntity} to drain to.
     * @param itemStack The item to get the required ticks for.
     * @return The required ticks.
     */
    public static int getRequiredTicks(BlockEntityTankInventory tile, ItemStack itemStack) {
        ResourceHandler<FluidResource> container = itemStack.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(itemStack));
        int amount = 0;
        if(container != null && IModHelpersNeoForge.get().getFluidHelpers().hasFluid(container))
            amount = IModHelpersNeoForge.get().getFluidHelpers().getAmount(IModHelpersNeoForge.get().getFluidHelpers().getFluid(container));
        int capacity = (int) Math.min(IModHelpersNeoForge.get().getFluidHelpers().getCapacity(container), tile.getTank().getFluidAmount());
        return (capacity - amount) / MB_PER_TICK;
    }

    @Override
    public boolean canTick(T tile, ItemStack itemStack, int slot, int tick) {
        boolean emptyContainer = false;
        ItemStack containerStack = tile.getInventory().getItem(slot);
        ResourceHandler<FluidResource> container = containerStack.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forHandlerIndex(VanillaContainerWrapper.of(tile.getInventory()), slot));
        if(container != null && IModHelpersNeoForge.get().getFluidHelpers().hasFluid(container)) {
            FluidStack fluidStack = IModHelpersNeoForge.get().getFluidHelpers().getFluid(container);
            if(IModHelpersNeoForge.get().getFluidHelpers().getAmount(fluidStack) <= 0)
                emptyContainer = true;
        } else emptyContainer = true;
        return super.canTick(tile, itemStack, slot, tick) && !emptyContainer;
    }

}
