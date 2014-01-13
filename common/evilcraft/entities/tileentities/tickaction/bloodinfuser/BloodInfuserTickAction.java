package evilcraft.entities.tileentities.tickaction.bloodinfuser;

import net.minecraft.item.ItemStack;
import evilcraft.CustomRecipe;
import evilcraft.entities.tileentities.IConsumeProduceWithTankTile;
import evilcraft.entities.tileentities.tickaction.ITickAction;

public abstract class BloodInfuserTickAction implements ITickAction<IConsumeProduceWithTankTile> {
    
    protected final static int MB_PER_TICK = 100;
    
    @Override
    public boolean canTick(IConsumeProduceWithTankTile tile, int tick) {
        return !tile.getTank().isEmpty()
                && getInfuseStack(tile) != null
                && (tile.getInventory().getStackInSlot(tile.getProduceSlot()) == null
                    || (tile.getInventory().getStackInSlot(tile.getProduceSlot()).itemID == willProduceItemID(tile)))
                && tile.canConsume(getInfuseStack(tile));
    }
    
    public ItemStack getInfuseStack(IConsumeProduceWithTankTile tile) {
        return tile.getInventory().getStackInSlot(tile.getConsumeSlot());
    }
    
    public abstract int willProduceItemID(IConsumeProduceWithTankTile tile);
    
    public boolean addToProduceSlot(IConsumeProduceWithTankTile tile, ItemStack itemStack) {
        ItemStack produceStack = tile.getInventory().getStackInSlot(tile.getProduceSlot());
        if(produceStack == null) {
            tile.getInventory().setInventorySlotContents(tile.getProduceSlot(), itemStack);
            return true;
        } else {
            if(produceStack.getItem() == itemStack.getItem()
               && produceStack.getMaxStackSize() >= produceStack.stackSize + itemStack.stackSize) {
                produceStack.stackSize += itemStack.stackSize;
                return true;
            }
        }
        return false;
    }
    
    public abstract int getRequiredTicks(IConsumeProduceWithTankTile tile);
    
}
