package evilcraft.entities.tileentities.tickaction.bloodinfuser;

import evilcraft.entities.tileentities.IConsumeProduceWithTankTile;
import evilcraft.entities.tileentities.tickaction.ITickAction;


public interface ITickActionWithTank<I extends IConsumeProduceWithTankTile> extends ITickAction<I> {
    
    public int getRequiredTicks(I tile);
    
}
