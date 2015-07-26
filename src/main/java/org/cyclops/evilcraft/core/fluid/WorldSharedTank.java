package org.cyclops.evilcraft.core.fluid;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.persist.world.WorldStorage;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;

/**
 * A tank that has shared contents for a given ID.
 * Based on World NBT storage.
 * @author rubensworks
 *
 */
public class WorldSharedTank extends SingleUseTank {

/**
     * The NBT name for the fluid tank.
     */
    public static final String NBT_TANKID = "tankID";
	
	protected String tankID = "";
	private int previousAmount = 0;
	
	/**
     * Make a new tank instance.
     * @param name The name for the tank, will be used for NBT storage.
     * @param capacity The capacity (mB) for the tank.
     * @param tile The TileEntity that uses this tank.
     */
    public WorldSharedTank(String name, int capacity, CyclopsTileEntity tile) {
        super(name, capacity, tile);
        this.tile = tile;
    }
    
    /**
     * Reset the previous fluid storage, used for interpolating fluid amounts client-side.
     */
    public void resetPreviousFluid() {
    	previousAmount = getFluidAmount();
    }
    
    /**
     * Get the previous fluid amount, used for interpolating fluid amounts client-side.
     * @return The previous amount.
     */
    public int getPreviousAmount() {
    	return previousAmount;
    }
    
    @Override
    public void writeTankToNBT(NBTTagCompound nbt) {
        super.writeTankToNBT(nbt);
        nbt.setString(NBT_TANKID, tankID);
    }

    @Override
    public void readTankFromNBT(NBTTagCompound nbt) {
        super.readTankFromNBT(nbt);
        tankID = nbt.getString(NBT_TANKID);
    }
    
    protected void readWorldFluid() {
    	this.fluid = WorldSharedTankCache.getInstance().getTankContent(tankID);
    }
    
    protected void writeWorldFluid() {
        if(!MinecraftHelpers.isClientSide()) {
            WorldSharedTankCache.getInstance().setTankContent(tankID, this.fluid);
        }
    }
    
    @Override
    public void setFluid(FluidStack fluid) {
    	super.setFluid(fluid);
    	writeWorldFluid();
    }
    
    @Override
    public FluidStack getFluid() {
    	readWorldFluid();
    	return super.getFluid();
    }
    
    @Override
    public int getFluidAmount() {
    	readWorldFluid();
    	return super.getFluidAmount();
    }
    
    @Override
    public int fill(FluidStack resource, boolean doFill) {
    	readWorldFluid();
    	int ret = super.fill(resource, doFill);
    	if(ret > 0 && doFill) {
    		writeWorldFluid();
    	}
    	return ret;
    }
    
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
    	readWorldFluid();
    	FluidStack ret = super.drain(maxDrain, doDrain);
    	if(ret != null && doDrain) {
    		writeWorldFluid();
    	}
    	return ret;
    }

    /**
     * Get the tank ID.
     * @return The tank ID.
     */
    public String getTankID() {
        return this.tankID;
    }

    public Fluid getAcceptedFluid() {
        return this.getFluidType();
    }

    @Override
    protected boolean replaceInnerFluid() {
        return false;
    }

    /**
     * Tank data stored in the world.
     * @author rubensworks
     */
    public static class TankData extends WorldStorage {

        /**
         * NBT key.
         */
        public static final String KEY = "WorldSharedTanks";
    	
    	/**
    	 * Make a new instance.
         * @param mod The mod.
    	 */
    	public TankData(ModBase mod) {
    		super(mod);
    	}

        @Override
        public void reset() {
            WorldSharedTankCache.getInstance().reset();
        }

        @Override
        protected String getDataId() {
            return KEY;
        }

        @Override
        public void readFromNBT(NBTTagCompound tag) {
            super.readFromNBT(tag);
            WorldSharedTankCache.getInstance().readFromNBT(tag);
        }

        @Override
        public void writeToNBT(NBTTagCompound tag) {
            super.writeToNBT(tag);
            WorldSharedTankCache.getInstance().writeToNBT(tag);
        }

    }
	
}
