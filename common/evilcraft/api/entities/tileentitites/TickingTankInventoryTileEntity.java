package evilcraft.api.entities.tileentitites;

import java.util.LinkedList;

import net.minecraftforge.fluids.Fluid;
import evilcraft.api.entities.tileentitites.tickaction.ITickAction;
import evilcraft.api.entities.tileentitites.tickaction.TickComponent;

/**
 * A TileEntity with Tank and Inventory that can tick.
 * @author rubensworks
 *
 */
public abstract class TickingTankInventoryTileEntity extends TankInventoryTileEntity {
    
    private LinkedList<TickComponent<IConsumeProduceEmptyInTankTile, ITickAction<IConsumeProduceEmptyInTankTile>>> tickers;
    
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
        super(inventorySize, inventoryName, tankSize, tankName, acceptedFluid);
        tickers = new LinkedList<TickComponent<IConsumeProduceEmptyInTankTile, ITickAction<IConsumeProduceEmptyInTankTile>>>();
    }
    
    /**
     * Add a new ticker.
     * @param ticker The ticker to (try) run every tick.
     * @return The index of the newly added ticker in the ticker list.
     */
    protected int addTicker(TickComponent<IConsumeProduceEmptyInTankTile, ITickAction<IConsumeProduceEmptyInTankTile>> ticker) {
        tickers.add(ticker);
        return tickers.size() - 1;
    }
    
    /**
     * Get the tickers this TileEntity has.
     * @return List of added tickers.
     */
    public LinkedList<TickComponent<IConsumeProduceEmptyInTankTile, ITickAction<IConsumeProduceEmptyInTankTile>>> getTickers() {
        return this.tickers;
    }
    
    @Override
    public boolean canUpdate() {
        return true;
    }
    
    @Override
    public void updateEntity() {
        super.updateEntity();
        for(TickComponent<IConsumeProduceEmptyInTankTile, ITickAction<IConsumeProduceEmptyInTankTile>> ticker : getTickers()) {
            ticker.tick(inventory.getStackInSlot(ticker.getSlot()));
        }
    }

}
