package evilcraft.api.gui.container;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.entities.tileentitites.TickingTankInventoryTileEntity;
import evilcraft.api.entities.tileentitites.tickaction.ITickAction;
import evilcraft.api.entities.tileentitites.tickaction.TickComponent;

/**
 * A container for a ticking tile entity with inventory.
 * @author rubensworks
 *
 * @param <T> The TickingTankInventoryTileEntity class.
 */
public class TickingTankInventoryContainer<T extends TickingTankInventoryTileEntity<T>> extends InventoryContainer<T>{

    private Map<TickComponent<T, ITickAction<T>>, Integer> previousTicks;
    private int lastTankAmount;
    
    private static final int INDEX_OFFSET = 1;
    private static final int INDEX_LIQUID_AMOUNT = 0;
    
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
        lastTankAmount = -1;
    }
    
    @Override
    public void addCraftingToCrafters(ICrafting icrafting) {
        super.addCraftingToCrafters(icrafting);
        sendTankUpdates(icrafting, true);
        sendTickersUpdates(icrafting, true);
    }
    
    private void sendTankUpdates(ICrafting icrafting, boolean force) {
        // Send tank content updates
        if(tile.getTank() != null &&
                (lastTankAmount != tile.getTank().getFluidAmount() || force)) {
            icrafting.sendProgressBarUpdate(this, INDEX_LIQUID_AMOUNT, tile.getTank().getFluidAmount());
        }
    }
    
    private void sendTickersUpdates(ICrafting icrafting, boolean force) {
        int index = 0;
        for(TickComponent<T, ITickAction<T>> ticker : tile.getTickers()) {
            if(previousTicks.get(ticker) != ticker.getTick() || force) {
                // Send current tick to client
                icrafting.sendProgressBarUpdate(this, INDEX_OFFSET + index, ticker.getTick());
                
                // Send the required ticks to the client with an index offset
                icrafting.sendProgressBarUpdate(this, INDEX_OFFSET + tile.getTickers().size() + index, ticker.getRequiredTicks());
            
                // And finally update the previous tick of this ticker.
                previousTicks.put(ticker, ticker.getTick());
            }
            index++;
        }
    }
    
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < this.crafters.size(); ++i) {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);
            sendTankUpdates(icrafting, false);
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
        } else {
            if(tile.getTank() != null) {
                if(index == INDEX_LIQUID_AMOUNT) {
                    if(tile.getTank().getFluid() == null) {
                        tile.getTank().setFluid(new FluidStack(tile.getTank().getAcceptedFluid(), value));
                    } else {
                        tile.getTank().getFluid().amount = value;
                    }
                }
            }
        }
    }

}
