package evilcraft.api.fluids;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * A simple tank that can accept and drain fluids until the capacity is reached.
 * Only one fluid can be accepted, which must be specified with {@link SingleUseTank#setAcceptedFluid(Fluid)}.
 * Based on the Buildcraft SingleUseTank.
 * @author rubensworks
 *
 */
public class SingleUseTank extends Tank {
    
    /**
     * The NBT name for the fluid tank.
     */
    public static final String NBT_ACCEPTED_FLUID = "acceptedFluid";
    
    private Fluid acceptedFluid;

    /**
     * Make a new tank instance.
     * @param name The name for the tank, will be used for NBT storage.
     * @param capacity The capacity (mB) for the tank.
     * @param tile The TileEntity that uses this tank.
     */
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

    /**
     * Reset the tank by setting the inner fluid to null.
     */
    public void reset() {
        acceptedFluid = null;
    }

    /**
     * Set the accepted fluid for this tank.
     * @param fluid The accepted fluid
     */
    public void setAcceptedFluid(Fluid fluid) {
        this.acceptedFluid = fluid;
    }

    /**
     * Get the accepted fluid for this tank.
     * @return The accepted fluid.
     */
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
