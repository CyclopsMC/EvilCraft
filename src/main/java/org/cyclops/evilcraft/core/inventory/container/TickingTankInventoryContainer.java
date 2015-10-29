package org.cyclops.evilcraft.core.inventory.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.inventory.container.TileInventoryContainer;
import org.cyclops.evilcraft.core.tileentity.TickingTankInventoryTileEntity;
import org.cyclops.evilcraft.core.tileentity.tickaction.ITickAction;
import org.cyclops.evilcraft.core.tileentity.tickaction.TickComponent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A container for a ticking tile entity with inventory.
 * @author rubensworks
 *
 * @param <T> The TickingTankInventoryTileEntity class.
 */
public class TickingTankInventoryContainer<T extends TickingTankInventoryTileEntity<T>> extends TileInventoryContainer<T> {

    private Map<TickComponent<T, ITickAction<T>>, Integer> previousTicks;
    
    private static final int INDEX_OFFSET = 1;
    
    /**
     * Make a new TickingInventoryContainer.
     * @param inventory The player inventory.
     * @param tile The TileEntity for this container.
     */
    public TickingTankInventoryContainer(InventoryPlayer inventory, T tile) {
        super(inventory, tile);
        previousTicks = new HashMap<TickComponent<T, ITickAction<T>>, Integer>();
        for(TickComponent<T, ITickAction<T>> ticker : tile.getTickers()) {
            previousTicks.put(ticker, ticker.getTick());
        }
    }
    
    @Override
    public void onCraftGuiOpened(ICrafting icrafting) {
        super.onCraftGuiOpened(icrafting);
        sendTickersUpdates(icrafting, true);
    }
    
    private void sendTickersUpdates(ICrafting icrafting, boolean force) {
        int index = 0;
        for(TickComponent<T, ITickAction<T>> ticker : tile.getTickers()) {
            if(previousTicks.get(ticker) != ticker.getTick() || force) {
                // Send current tick to client
                icrafting.sendProgressBarUpdate(this, INDEX_OFFSET + index, ticker.getTick());
                
                // Send the required ticks to the client with an index offset
                icrafting.sendProgressBarUpdate(this, INDEX_OFFSET + tile.getTickers().size() + index, Math.round(ticker.getRequiredTicks()));
            
                // And finally update the previous tick of this ticker.
                previousTicks.put(ticker, ticker.getTick());
            }
            index++;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (ICrafting icrafting : (List<ICrafting>) crafters) {
            sendTickersUpdates(icrafting, false);
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int index, int value) {
        // This will receive the current tick, the required ticks for this crafting or the contents of the tank.
        if(index >= tile.getTickers().size() + INDEX_OFFSET) {
            tile.getTickers().get(index - tile.getTickers().size() - INDEX_OFFSET).setRequiredTicks(value);
        } else if(index >= INDEX_OFFSET) {
            tile.getTickers().get(index - INDEX_OFFSET).setTick(value);
        }
    }

}
