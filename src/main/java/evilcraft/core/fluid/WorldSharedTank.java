package evilcraft.core.fluid;

import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.core.tileentity.EvilCraftTileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fluids.FluidStack;

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
    public WorldSharedTank(String name, int capacity, EvilCraftTileEntity tile) {
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

    /**
     * Tank data stored in the world.
     * @author rubensworks
     */
    public static class TankData extends WorldSavedData {

        /**
         * NBT key.
         */
        public static final String KEY = "WorldSharedTanks";
    	
    	private NBTTagCompound tankTag = null;
    	
    	/**
    	 * Make a new instance.
    	 * @param key The key.
    	 */
    	public TankData(String key) {
    		super(key);
            this.tankTag = new NBTTagCompound();
    	}
    	
    	/**
    	 * @return The tank tag.
    	 */
    	public NBTTagCompound getTankTag() {
    		return this.tankTag;
    	}
    	
    	@Override
        public void readFromNBT(NBTTagCompound tag) {
    		tankTag = tag.getCompoundTag("Tank");
        }

        @Override
        public void writeToNBT(NBTTagCompound tag) {
        	tag.setTag("Tank", tankTag);
        }
    	
    }
	
}
