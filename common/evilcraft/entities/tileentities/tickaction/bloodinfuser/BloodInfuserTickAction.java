package evilcraft.entities.tileentities.tickaction.bloodinfuser;

import net.minecraft.item.ItemStack;
import evilcraft.entities.tileentities.IConsumeProduceWithTankTile;
import evilcraft.entities.tileentities.tickaction.ITickAction;

public abstract class BloodInfuserTickAction implements ITickAction<IConsumeProduceWithTankTile> {
    
    @Override
    public boolean canTick(IConsumeProduceWithTankTile tile, int tick) {
        return tile.getInventory().getStackInSlot(tile.getProduceSlot()) == null
                && !tile.getTank().isEmpty()
                && getInfuseStack(tile) != null
                && tile.canConsume(getInfuseStack(tile));
    }
    
    public ItemStack getInfuseStack(IConsumeProduceWithTankTile tile) {
        return tile.getInventory().getStackInSlot(tile.getConsumeSlot());
    }
    
}
