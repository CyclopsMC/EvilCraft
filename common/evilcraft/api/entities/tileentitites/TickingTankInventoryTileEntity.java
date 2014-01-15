package evilcraft.api.entities.tileentitites;

import java.util.LinkedList;

import net.minecraftforge.fluids.Fluid;
import evilcraft.api.entities.tileentitites.tickaction.ITickAction;
import evilcraft.api.entities.tileentitites.tickaction.TickComponent;

public class TickingTankInventoryTileEntity extends TankInventoryTileEntity {
    
    private LinkedList<TickComponent<IConsumeProduceEmptyInTankTile, ITickAction<IConsumeProduceEmptyInTankTile>>> tickers;
    
    public TickingTankInventoryTileEntity(int inventorySize,
            String inventoryName, int tankSize, String tankName, Fluid acceptedFluid) {
        super(inventorySize, inventoryName, tankSize, tankName, acceptedFluid);
        tickers = new LinkedList<TickComponent<IConsumeProduceEmptyInTankTile, ITickAction<IConsumeProduceEmptyInTankTile>>>();
    }
    
    protected void addTicker(TickComponent<IConsumeProduceEmptyInTankTile, ITickAction<IConsumeProduceEmptyInTankTile>> ticker) {
        tickers.add(ticker);
    }
    
    public LinkedList<TickComponent<IConsumeProduceEmptyInTankTile, ITickAction<IConsumeProduceEmptyInTankTile>>> getTickers() {
        return this.tickers;
    }

}
