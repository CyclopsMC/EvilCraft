package org.cyclops.evilcraft.core.fluid;

import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.fluid.SingleUseTank;

/**
 * A single use tank that can accept multiple types of fluids.
 * @author rubensworks
 *
 */
public class ImplicitFluidConversionTank extends SingleUseTank {

    private ImplicitFluidConverter converter;

    public ImplicitFluidConversionTank(int capacity, ImplicitFluidConverter converter) {
        super(capacity);
        this.converter = converter;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (!canFillFluidType(resource)) {
            return 0;
        }

        FluidStack converted = converter.convert(resource);
        if(converted.isEmpty()) {
            return 0;
        }
        double ratio = !resource.isEmpty() ? converter.getRatio(resource.getFluid()) : 1;
        return (int) Math.ceil(super.fill(converted, action) / ratio);
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (!canDrainFluidType(resource)) {
            return FluidStack.EMPTY;
        }

        return super.drain(resource, action);
    }

    public boolean canFillFluidType(FluidStack fluid) {
        return fluid.isEmpty() || fluid.getFluid() == converter.getTarget() || converter.canConvert(fluid.getFluid());
    }

    public boolean canDrainFluidType(FluidStack fluid) {
        return fluid.isEmpty() || fluid.getFluid() == converter.getTarget() || converter.canConvert(fluid.getFluid());
    }

}
