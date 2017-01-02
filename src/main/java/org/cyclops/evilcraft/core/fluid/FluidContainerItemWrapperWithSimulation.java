package org.cyclops.evilcraft.core.fluid;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.capability.fluid.FluidHandlerItemCapacity;

/**
 * Safer version of {@link FluidHandlerItemCapacity} that makes sure that simulated fluidstacks are
 * not filled/drained without simulation. This must be used for tanks that are omni-present,
 * meaning that their fluid data is independent of the itemstack.
 * This only works for containers that do NOT swap their item when filling/draining.
 * @author rubensworks
 */
public class FluidContainerItemWrapperWithSimulation extends FluidHandlerItemCapacity {
    public FluidContainerItemWrapperWithSimulation(ItemStack container, int capacity) {
        super(container, capacity);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        // Extremely nasty hack incoming, Forge supports no clean way of doing this cleanly unfortunately...
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[2];
        if (stackTraceElement.getClassName().equals("net.minecraftforge.fluids.FluidUtil")
                && stackTraceElement.getMethodName().equals("tryFillContainer")) {
            doFill = false;
        }

        if (resource instanceof SimulatedFluidStack) {
            doFill = false;
        }
        return super.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        FluidStack drained = super.drain(maxDrain, doDrain);
        if (doDrain || drained == null || drained.amount == 0) {
            return drained;
        } else {
            return new SimulatedFluidStack(drained.getFluid(), drained.amount);
        }
    }

    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        if (resource instanceof SimulatedFluidStack) {
            doDrain = false;
        }
        FluidStack drained = super.drain(resource, doDrain);
        if (doDrain || drained == null || drained.amount == 0) {
            return drained;
        } else {
            return new SimulatedFluidStack(drained.getFluid(), drained.amount);
        }
    }
}
