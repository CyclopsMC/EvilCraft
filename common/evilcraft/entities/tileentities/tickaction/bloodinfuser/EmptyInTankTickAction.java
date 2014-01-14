package evilcraft.entities.tileentities.tickaction.bloodinfuser;

import net.minecraft.item.ItemStack;
import evilcraft.entities.tileentities.IConsumeProduceEmptyInTankTile;

public abstract class EmptyInTankTickAction implements ITickActionWithTank<IConsumeProduceEmptyInTankTile> {
    
    protected final static int MB_PER_TICK = 100;
    
    @Override
    public boolean canTick(IConsumeProduceEmptyInTankTile tile, int tick) {
        return !tile.getTank().isFull()
                && getEmptyStack(tile) != null
                && getEmptyStack(tile).stackSize == 1;
    }
    
    public ItemStack getEmptyStack(IConsumeProduceEmptyInTankTile tile) {
        return tile.getInventory().getStackInSlot(tile.getEmptyToTankSlot());
    }
    
}
