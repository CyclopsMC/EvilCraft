package evilcraft.entities.tileentities.tickaction;

import evilcraft.entities.tileentities.IConsumeProduceTile;

public interface ITickAction<I extends IConsumeProduceTile> {
    
    public boolean canTick(I tile, int tick);
    public void onTick(I tile, int tick);
    
}
