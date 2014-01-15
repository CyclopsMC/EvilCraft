package evilcraft.api.entities.tileentitites;

import evilcraft.api.fluids.SingleUseTank;

public interface IConsumeProduceWithTankTile extends IConsumeProduceTile {
    
    public SingleUseTank getTank();
    
}
