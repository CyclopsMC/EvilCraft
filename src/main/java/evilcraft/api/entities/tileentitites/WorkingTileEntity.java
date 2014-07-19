package evilcraft.api.entities.tileentitites;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

/**
 * A TileEntity with that processes items with inventory and tank.
 * @author rubensworks
 * @param <T> The subclass of {@link TankInventoryTileEntity}, will be in
 * most cases just the extension class.
 * @see TickingTankInventoryTileEntity
 */
public abstract class WorkingTileEntity<T extends TankInventoryTileEntity> extends TickingTankInventoryTileEntity<T>{
	
	/**
     * Make a new instance.
     * @param inventorySize Amount of slots in the inventory.
     * @param inventoryName Internal name of the inventory.
     * @param tankSize Size (mB) of the tank.
     * @param tankName Internal name of the tank.
     * @param acceptedFluid Type of Fluid to accept.
     */
	public WorkingTileEntity(int inventorySize, String inventoryName,
			int tankSize, String tankName, Fluid acceptedFluid) {
		super(inventorySize, inventoryName, tankSize, tankName, acceptedFluid);
	}
	
	 /**
     * Check if the given item can be infused.
     * @param itemStack The item to check.
     * @return If it can be infused.
     */
    public abstract boolean canConsume(ItemStack itemStack);
    
    /**
     * Check if this tile is valid and can start working.
     * Mostly defined by environmental parameters.
     * @return If it is valid and can work.
     */
    public abstract boolean canWork();
    
    /**
     * If this tile is working.
     * @return If it is abstract.
     */
    public boolean isWorking() {
    	return getWorkTick() > 0;
    }
    
    /**
     * If the furnace should visually (block icon) show it is working, should only be
     * called client-side.
     * @return If the state is working.
     */
    public boolean isVisuallyWorking() {
        return getCurrentState() == 1;
    }
    
    /**
     * Get the work progress scaled, to be used in GUI's.
     * @param scale The scale this progress should be applied to.
     * @return The scaled working progress.
     */
    public int getWorkTickScaled(int scale) {
    	return (int) ((float)getWorkTick() / (float)getRequiredWorkTicks() * (float)scale);
    }
    
    protected abstract int getWorkTicker();
    
    protected int getWorkTick() {
        return getTickers().get(getWorkTicker()).getTick();
    }
    
    protected int getRequiredWorkTicks() {
        return getTickers().get(getWorkTicker()).getRequiredTicks();
    }
    
    /**
     * Resets the ticks of the work.
     */
    public void resetWork() {
        getTickers().get(getWorkTicker()).setTick(0);
        getTickers().get(getWorkTicker()).setRequiredTicks(0);
    }

    @Override
    public int getNewState() {
        return this.isWorking()?1:0;
    }

    @Override
    public void onStateChanged() {
        sendUpdate();
    }

}
