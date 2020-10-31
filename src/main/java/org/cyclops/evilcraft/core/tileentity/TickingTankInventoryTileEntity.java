package org.cyclops.evilcraft.core.tileentity;

import com.google.common.collect.Lists;
import lombok.experimental.Delegate;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntityType;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.evilcraft.core.tileentity.tickaction.ITickAction;
import org.cyclops.evilcraft.core.tileentity.tickaction.TickComponent;

import java.util.List;

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
    
    private List<TickComponent<T, ITickAction<T>>> tickers;
    private int currentState = -1;
    private int previousState = -1;

    public TickingTankInventoryTileEntity(TileEntityType<?> type, int inventorySize, int stackSize, int tankSize, Fluid acceptedFluid) {
        super(type, inventorySize, stackSize, tankSize, acceptedFluid);
        tickers = Lists.newArrayList();
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
    public List<TickComponent<T, ITickAction<T>>> getTickers() {
        return this.tickers;
    }

    @Override
    public void read(CompoundNBT data) {
        super.read(data);
        currentState = data.getInt("currentState");
        ListNBT tickerList = data.getList("tickers", 10);
        for(int i = 0; i < tickers.size(); i++) {
            TickComponent<T, ITickAction<T>> ticker = tickers.get(i);
            if(tickerList.size() > i) {
                CompoundNBT tag = tickerList.getCompound(i);
                ticker.setTick(tag.getInt("tick"));
                ticker.setRequiredTicks(tag.getFloat("requiredTicks"));
            }
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT data) {
        data = super.write(data);
        data.putInt("currentState", currentState);
        ListNBT tickerList = new ListNBT();
        for(TickComponent<T, ITickAction<T>> ticker : tickers) {
            CompoundNBT tag = new CompoundNBT();
            tag.putInt("tick", ticker.getTick());
            tag.putFloat("requiredTicks", ticker.getRequiredTicks());
            tickerList.add(tag);
        }
        data.put("tickers", tickerList);
        return data;
    }
    
    @Override
    public void updateTileEntity() {
        super.updateTileEntity();
        
        // Update tickers.
        if(!world.isRemote()) {
            boolean redstone = world.isBlockPowered(getPos());
            for(TickComponent<T, ITickAction<T>> ticker : getTickers()) {
                if(!(ticker.isRedstoneDisableable() && redstone)) {
                    ticker.tick(getInventory().getStackInSlot(ticker.getSlot()), ticker.getSlot());
                }
            }
        }
        
        if(!world.isRemote()) {
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
