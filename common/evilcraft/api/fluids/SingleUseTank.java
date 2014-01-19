package evilcraft.api.fluids;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * Based on the Buildcraft SingleUseTank.
 * @author rubensworks
 *
 */
public class SingleUseTank extends Tank {
    
    public static final String NBT_ACCEPTED_FLUID = "acceptedFluid";
    
    private Fluid acceptedFluid;

    public SingleUseTank(String name, int capacity, TileEntity tile) {
        super(name, capacity, tile);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if (resource == null)
            return 0;
        if (doFill && acceptedFluid == null)
            acceptedFluid = resource.getFluid();
        if (acceptedFluid == null || acceptedFluid == resource.getFluid())
            return super.fill(resource, doFill);
        return 0;
    }

    public void reset() {
        acceptedFluid = null;
    }

    public void setAcceptedFluid(Fluid fluid) {
        this.acceptedFluid = fluid;
    }

    public Fluid getAcceptedFluid() {
        return acceptedFluid;
    }

    @Override
    public void writeTankToNBT(NBTTagCompound nbt) {
        super.writeTankToNBT(nbt);
        if (acceptedFluid != null)
            nbt.setString(NBT_ACCEPTED_FLUID, acceptedFluid.getName());
    }

    @Override
    public void readTankFromNBT(NBTTagCompound nbt) {
        super.readTankFromNBT(nbt);
        acceptedFluid = FluidRegistry.getFluid(nbt.getString(NBT_ACCEPTED_FLUID));
    }
}
