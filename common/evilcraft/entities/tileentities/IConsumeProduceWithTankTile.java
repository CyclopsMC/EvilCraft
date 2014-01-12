package evilcraft.entities.tileentities;

import evilcraft.api.fluids.Tank;

public interface IConsumeProduceWithTankTile extends IConsumeProduceTile {
    
    public Tank getTank();
    
}
