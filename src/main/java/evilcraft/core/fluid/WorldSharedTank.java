package evilcraft.core.fluid;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import evilcraft.core.tileentity.EvilCraftTileEntity;

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
    public static final String NBT_TANKID = "acceptedFluid";
	
	protected String tankID;
	private int previousAmount = 0;
	
	/**
     * Make a new tank instance.
     * @param name The name for the tank, will be used for NBT storage.
     * @param capacity The capacity (mB) for the tank.
     * @param tile The TileEntity that uses this tank.
     * @param tankID The unique tank ID.
     */
    public WorldSharedTank(String name, int capacity, EvilCraftTileEntity tile, String tankID) {
        super(name, capacity, tile);
        this.tile = tile;
        this.tankID = tankID;
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
    
    protected void writeToWorld() {
    	FluidStack oldFluid = getFluid();
    	String key = tankID;
    	WorldServer world = MinecraftServer.getServer().worldServers[0];
    	TankData data = (TankData) world.loadItemData(TankData.class, key);
    	
    	NBTTagCompound tankTag = new NBTTagCompound();
    	writeTankToNBT(tankTag);
    	
    	if(data == null) {
    		data = new TankData(key);
    		data.setTankTag(tankTag);
    		world.setItemData(key, data);
    	}
    	
    	data.setTankTag(tankTag);
    	data.markDirty();
    }
    
    protected void readFromWorld() {
    	String key = tankID;
    	WorldServer world = MinecraftServer.getServer().worldServers[0];
    	TankData data = (TankData) world.loadItemData(TankData.class, key);
    	
    	if(data == null) {
    		data = new TankData(key);
    		world.setItemData(key, data);
    	}
    	
    	NBTTagCompound tankTag = data.getTankTag();
    	if(tankTag != null) {
    		readTankFromNBT(tankTag);
    	}
    	
    	WorldSharedTankCache.getInstance().setTankContent(tankID, fluid);
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
    	if(MinecraftServer.getServer() != null) {
    		writeToWorld();
    	}
    	return super.writeToNBT(nbt);
    }
    
    @Override
    public FluidTank readFromNBT(NBTTagCompound nbt) {
    	if(MinecraftServer.getServer() != null) {
    		readFromWorld();
    	}
    	return super.readFromNBT(nbt);
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
    	WorldSharedTankCache.getInstance().setTankContent(tankID, this.fluid);
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
    	if(ret > 0) {
    		writeWorldFluid();
    	}
    	return ret;
    }
    
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
    	readWorldFluid();
    	FluidStack ret = super.drain(maxDrain, doDrain);
    	if(ret != null) {
    		writeWorldFluid();
    	}
    	return ret;
    }
    
    /**
     * Tank data stored in the world.
     * @author rubensworks
     */
    public static class TankData extends WorldSavedData {
    	
    	private NBTTagCompound tankTag = null;
    	
    	/**
    	 * Make a new instance.
    	 * @param tankID The unique tank ID.
    	 */
    	public TankData(String tankID) {
    		super(tankID);
    	}
    	
    	/**
    	 * Set the tank data.
    	 * @param tankTag The tank data.
    	 */
    	public void setTankTag(NBTTagCompound tankTag) {
    		this.tankTag = tankTag;
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
