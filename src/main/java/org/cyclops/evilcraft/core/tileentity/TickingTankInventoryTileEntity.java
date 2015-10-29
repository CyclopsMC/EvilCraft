package org.cyclops.evilcraft.core.tileentity;

import lombok.experimental.Delegate;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.Fluid;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.cyclopscore.tileentity.TankInventoryTileEntity;
import org.cyclops.evilcraft.core.tileentity.tickaction.ITickAction;
import org.cyclops.evilcraft.core.tileentity.tickaction.TickComponent;

import java.util.LinkedList;

/**
 * A TileEntity with Tank and Inventory that can tick.
 * It uses a list of {@link TickComponent} that are able to tick.
 * And these components will contain a collection of {@link ITickAction}
 * that can perform specific actions depending on the condition of the {@link TickComponent}.
 * @author rubensworks
 * @param <T> The subclass of {@link TankInventoryTileEntity}, will be in
 * most cases just the extension class.
 * @see TickComponent
 */
public abstract class TickingTankInventoryTileEntity<T extends TankInventoryTileEntity> extends TankInventoryTileEntity
        implements CyclopsTileEntity.ITickingTile {

    @Delegate
    protected final ITickingTile tickingTileComponent = new TickingTileComponent(this);
    
    private LinkedList<TickComponent<T, ITickAction<T>>> tickers;
    private int currentState = -1;
    private int previousState = -1;

    /**
     * Make a new TickingTankInventoryTileEntity.
     * @param inventorySize Amount of slots in the inventory.
     * @param inventoryName Internal name of the inventory.
     * @param tankSize Size (mB) of the tank.
     * @param tankName Internal name of the tank.
     * @param acceptedFluid Type of Fluid to accept.
     * @param stackSize The maximum stacksize each slot can have.
     */
    public TickingTankInventoryTileEntity(int inventorySize,
                                          String inventoryName, int tankSize, String tankName, Fluid acceptedFluid, int stackSize) {
        super(inventorySize, inventoryName, stackSize, tankSize, tankName, acceptedFluid);
        tickers = new LinkedList<TickComponent<T, ITickAction<T>>>();
    }
    
    /**
     * Make a new TickingTankInventoryTileEntity.
     * @param inventorySize Amount of slots in the inventory.
     * @param inventoryName Internal name of the inventory.
     * @param tankSize Size (mB) of the tank.
     * @param tankName Internal name of the tank.
     * @param acceptedFluid Type of Fluid to accept.
     */
    public TickingTankInventoryTileEntity(int inventorySize,
            String inventoryName, int tankSize, String tankName, Fluid acceptedFluid) {
        this(inventorySize, inventoryName, tankSize, tankName, acceptedFluid, 64);
    }
    
    /**
     * Add a new ticker.
     * @param ticker The ticker to (try) run every tick.
     * @return The index of the newly added ticker in the ticker list.
     */
    protected int addTicker(TickComponent<T, ITickAction<T>> ticker) {
        tickers.add(ticker);
        return tickers.size() - 1;
    }
    
    /**
     * Get the tickers this TileEntity has.
     * @return List of added tickers.
     */
    public LinkedList<TickComponent<T, ITickAction<T>>> getTickers() {
        return this.tickers;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        currentState = data.getInteger("currentState");
        NBTTagList tickerList = data.getTagList("tickers", 10);
        for(int i = 0; i < tickers.size(); i++) {
            TickComponent<T, ITickAction<T>> ticker = tickers.get(i);
            if(tickerList.tagCount() > i) {
                NBTTagCompound tag = tickerList.getCompoundTagAt(i);
                if(tag != null) {
                    ticker.setTick(tag.getInteger("tick"));
                    ticker.setRequiredTicks(tag.getFloat("requiredTicks"));
                }
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        data.setInteger("currentState", currentState);
        NBTTagList tickerList = new NBTTagList();
        for(TickComponent<T, ITickAction<T>> ticker : tickers) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("tick", ticker.getTick());
            tag.setFloat("requiredTicks", ticker.getRequiredTicks());
            tickerList.appendTag(tag);
        }
        data.setTag("tickers", tickerList);
    }
    
    @Override
    public void updateTileEntity() {
        super.updateTileEntity();
        
        // Update tickers.
        if(!worldObj.isRemote) {
            boolean redstone = worldObj.isBlockPowered(getPos());
            for(TickComponent<T, ITickAction<T>> ticker : getTickers()) {
                if(!(ticker.isRedstoneDisableable() && redstone)) {
                    ticker.tick(inventory.getStackInSlot(ticker.getSlot()), ticker.getSlot());
                }
            }
        }
        
        if(!worldObj.isRemote) {
            // Update state server->clients.
            int newState = getNewState();
            if(newState != currentState) {
                currentState = newState;
                onStateChanged();
            }
        } else {
            // Update internal state for client.
            if(previousState != currentState) {
                previousState = currentState;
                onStateChanged();
            }
        }
    }
    
    /**
     * Get the new (numerical) state for this tile entity.
     * @return The new state.
     */
    public abstract int getNewState();
    /**
     * What needs to happen when the (numerical) state is changed.
     */
    public abstract void onStateChanged();

    /**
     * Get the current (numerical) state.
     * @return The current tile entity state.
     */
    public int getCurrentState() {
        return currentState;
    }

}
