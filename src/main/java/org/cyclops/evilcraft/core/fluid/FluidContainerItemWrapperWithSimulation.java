package org.cyclops.evilcraft.core.fluid;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
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

    public FluidContainerItemWrapperWithSimulation(ItemStack container, int capacity, Fluid fluid) {
        super(container, capacity, fluid);
    }

    protected FluidAction shouldDoFill(FluidStack resource, FluidAction doFill) {
        if (doFill.simulate()) {
            return FluidAction.SIMULATE;
        }
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        // Hacky hack to check if we're inside the part of FluidUtil that acts on the copy of the item to check filling...
        if ("net.neoforged.neoforge.fluids.FluidUtil".equals(stackTraceElements[3].getClassName()) && "lambda$tryFillContainer$1".equals(stackTraceElements[3].getMethodName())) {
            doFill = FluidAction.SIMULATE;
        }
        return doFill;
    }

    @Override
    public int fill(FluidStack resource, FluidAction doFill) {
        doFill = shouldDoFill(resource, doFill);
        return super.fill(resource, doFill);
    }

    protected FluidStack wrapSimulatedDrained(FluidStack drained, FluidAction doDrain) {
        if (doDrain.execute() || drained.isEmpty() || drained.getAmount() == 0) {
            return drained;
        } else {
            return asSimulatedFluidStack(new FluidStack(drained.getFluid(), drained.getAmount()));
        }
    }

    public static FluidStack asSimulatedFluidStack(FluidStack fluidStack) {
//        fluidStack.set(RegistryEntries.COMPONENT_SIMULATED, true);
        return fluidStack;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction doDrain) {
        FluidStack drained = super.drain(maxDrain, doDrain);
        return wrapSimulatedDrained(drained, doDrain);
    }

    protected FluidAction shouldDoDrain(FluidStack resource, FluidAction doDrain) {
        if (doDrain.simulate()) {
            return FluidAction.SIMULATE;
        }
        /*if (resource instanceof SimulatedFluidStack) {
            doDrain = FluidAction.SIMULATE;
        }*/
        return doDrain;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction doDrain) {
        doDrain = shouldDoDrain(resource, doDrain);
        return wrapSimulatedDrained(super.drain(resource, doDrain), doDrain);
    }
}
