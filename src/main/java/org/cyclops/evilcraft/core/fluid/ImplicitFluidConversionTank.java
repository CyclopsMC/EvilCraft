package org.cyclops.evilcraft.core.fluid;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
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
    public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
        if (!canFillFluidType(resource)) {
            return 0;
        }

        FluidStack converted = converter.convert(resource.toStack(amount));
        if(converted.isEmpty()) {
            return 0;
        }
        double ratio = !resource.isEmpty() ? converter.getRatio(resource.getFluid()) : 1;
        return (int) Math.ceil(super.insert(index, FluidResource.of(converted), converted.getAmount(), transaction) / ratio);
    }

    @Override
    public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
        if (!canDrainFluidType(resource)) {
            return 0;
        }
        return super.extract(index, resource, amount, transaction);
    }

    public boolean canFillFluidType(FluidResource fluid) {
        return fluid.isEmpty() || fluid.getFluid() == converter.getTarget() || converter.canConvert(fluid.getFluid());
    }

    public boolean canDrainFluidType(FluidResource fluid) {
        return fluid.isEmpty() || fluid.getFluid() == converter.getTarget() || converter.canConvert(fluid.getFluid());
    }

}
