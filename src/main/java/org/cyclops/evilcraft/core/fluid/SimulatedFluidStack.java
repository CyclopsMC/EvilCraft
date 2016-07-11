package org.cyclops.evilcraft.core.fluid;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 * Marker for FluidStacks that are simulated.
 * @author rubensworks
 */
public class SimulatedFluidStack extends FluidStack {
    public SimulatedFluidStack(Fluid fluid, int amount) {
        super(fluid, amount);
    }

    public SimulatedFluidStack(Fluid fluid, int amount, NBTTagCompound nbt) {
        super(fluid, amount, nbt);
    }

    public SimulatedFluidStack(FluidStack stack, int amount) {
        super(stack, amount);
    }
}
