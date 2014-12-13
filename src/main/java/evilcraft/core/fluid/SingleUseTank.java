package evilcraft.core.fluid;

import evilcraft.core.tileentity.EvilCraftTileEntity;
import evilcraft.core.tileentity.TankInventoryTileEntity;
import net.minecraft.nbt.NBTTagCompound;
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
    protected EvilCraftTileEntity tile;

    /**
     * Make a new tank instance.
     * @param name The name for the tank, will be used for NBT storage.
     * @param capacity The capacity (mB) for the tank.
     * @param tile The TileEntity that uses this tank.
     */
    public SingleUseTank(String name, int capacity, EvilCraftTileEntity tile) {
        super(name, capacity, tile);
        this.tile = tile;
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        Fluid acceptedFluid = getAcceptedFluid();
        if(getFluid() == null && acceptedFluid != null) {
    		acceptedFluid = null;
    	}    	
    	int filled = 0;
        if (resource == null) {
        	filled = 0;
        } else {
        	if (doFill && acceptedFluid == null) {
        		acceptedFluid = resource.getFluid();
        	}
        	if (acceptedFluid == null || acceptedFluid == resource.getFluid()) {
                filled = super.fill(resource, doFill);
            }
        }
        if(filled > 0) {
        	sendUpdate();
        }
        return filled;
    }
    
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
    	FluidStack drained = super.drain(maxDrain, doDrain);
    	if(drained != null) {
    		sendUpdate();
    	}
    	return drained;
    }
    
    protected void sendUpdate() {
    	// TODO: generalize this to accept ITankUpdateListeners if this would be necessary later.
    	if(!(tile instanceof TankInventoryTileEntity) || ((TankInventoryTileEntity) tile).isSendUpdateOnTankChanged()) {
    		tile.sendUpdate();
    	}
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
    
    /**
     * If this tank can accept the given fluid.
     * @param fluid The fluid that needs to be checked.
     * @return If this tank can accept it.
     */
    public boolean canTankAccept(Fluid fluid) {
    	return getAcceptedFluid().equals(fluid);
    }
    
    /**
     * If this tank can completely contain the given fluid.
     * @param fluidStack The fluid to try to fill.
     * @return If this tank can completely contain the given fluid.
     */
    public boolean canCompletelyFill(FluidStack fluidStack) {
    	int amount = (fluidStack != null) ? fluidStack.amount : 0;
    	return getFluidAmount() + amount <= getCapacity();
    }
    
}
