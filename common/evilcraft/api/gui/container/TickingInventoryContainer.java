package evilcraft.api.gui.container;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
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
public class TickingInventoryContainer<T extends TickingTankInventoryTileEntity<T>> extends InventoryContainer<T>{

    private Map<TickComponent<T, ITickAction<T>>, Integer> containerTickers;
    
    /**
     * Make a new TickingInventoryContainer.
     * @param inventory The player inventory.
     * @param tile The TileEntity for this container.
     */
    public TickingInventoryContainer(InventoryPlayer inventory, T tile) {
        super(inventory, tile);
        containerTickers = new HashMap<TickComponent<T, ITickAction<T>>, Integer>();
        for(TickComponent<T, ITickAction<T>> ticker : tile.getTickers()) {
            containerTickers.put(ticker, ticker.getTick());
        }
    }
    
    @Override
    public void addCraftingToCrafters(ICrafting crafter) {
        super.addCraftingToCrafters(crafter);
        int i = 0;
        for(TickComponent<T, ITickAction<T>> ticker : tile.getTickers()) {
            crafter.sendProgressBarUpdate(this, i, ticker.getTick());
            i++;
        }
    }
    
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < this.crafters.size(); ++i) {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);

            for(TickComponent<T, ITickAction<T>> ticker : tile.getTickers()) {
                if(containerTickers.get(ticker) != ticker.getTick()) {
                    icrafting.sendProgressBarUpdate(this, i, ticker.getTick());
                }
            }
        }
        
        for(TickComponent<T, ITickAction<T>> ticker : tile.getTickers()) {
            containerTickers.put(ticker, ticker.getTick());
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int index, int value) {
        tile.getTickers().get(index).setTick(value);
    }

}
