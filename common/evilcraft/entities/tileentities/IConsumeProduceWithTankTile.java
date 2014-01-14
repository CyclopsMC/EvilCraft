package evilcraft.entities.tileentities;

import evilcraft.api.fluids.SingleUseTank;

public interface IConsumeProduceWithTankTile extends IConsumeProduceTile {
    
    public SingleUseTank getTank();
    
}
