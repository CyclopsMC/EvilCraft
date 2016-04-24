package org.cyclops.evilcraft.core.fluid;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;

/**
 * A single use tank that can accept multiple types of fluids.
 * @author rubensworks
 *
 */
public class ImplicitFluidConversionTank extends SingleUseTank {

	private ImplicitFluidConverter converter;
	
	/**
     * Make a new tank instance.
     * @param name The name for the tank, will be used for NBT storage.
     * @param capacity The capacity (mB) for the tank.
     * @param tile The TileEntity that uses this tank.
	 * @param converter The fluid converter.
     */
    public ImplicitFluidConversionTank(String name, int capacity, CyclopsTileEntity tile,
    		ImplicitFluidConverter converter) {
        super(name, capacity, tile);
        this.converter = converter;
    }
    
    @Override
    public int fill(FluidStack resource, boolean doFill) {
    	FluidStack converted = converter.convert(resource);
        if(converted == null) {
            return 0;
        }
        double ratio = resource != null ? converter.getRatio(resource.getFluid()) : 1;
        return (int) Math.ceil(super.fill(converted, doFill) / ratio);
    }
    
    @Override
    public boolean canTankAccept(Fluid fluid) {
    	return super.canTankAccept(fluid) || converter.canConvert(fluid);
    }
    
    @Override
    public boolean canCompletelyFill(FluidStack fluidStack) {
    	FluidStack converted = converter.convert(fluidStack);
    	return super.canCompletelyFill(converted);
    }
	
}
