package evilcraft.api.entities.tileentitites.tickaction;

import evilcraft.api.entities.tileentitites.IConsumeProduceTile;

public interface ITickAction<I extends IConsumeProduceTile> {
    
    public boolean canTick(I tile, int tick);
    public void onTick(I tile, int tick);
    public int getRequiredTicks(I tile);
    
}
