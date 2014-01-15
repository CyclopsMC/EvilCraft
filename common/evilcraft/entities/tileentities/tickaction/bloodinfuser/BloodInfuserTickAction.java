package evilcraft.entities.tileentities.tickaction.bloodinfuser;

import net.minecraft.item.ItemStack;
import evilcraft.api.entities.tileentitites.IConsumeProduceEmptyInTankTile;
import evilcraft.api.entities.tileentitites.tickaction.ITickAction;

public abstract class BloodInfuserTickAction implements ITickAction<IConsumeProduceEmptyInTankTile> {
    
    protected final static int MB_PER_TICK = 100;
    
    @Override
    public boolean canTick(IConsumeProduceEmptyInTankTile tile, int tick) {
        // Only allow ticking if production slot is empty or if the producing item is the same and
        // there is at least one spot left in the stack.
        if(!tile.getTank().isEmpty() && getInfuseStack(tile) != null && tile.canConsume(getInfuseStack(tile))) {
            ItemStack production = tile.getInventory().getStackInSlot(tile.getProduceSlot());
            if(production == null) {
                return true;
            } else if(production.itemID == willProduceItemID(tile)) {
                if(production.stackSize < production.getMaxStackSize())
                    return true;
            }                
        }
        return false;
    }
    
    public ItemStack getInfuseStack(IConsumeProduceEmptyInTankTile tile) {
        return tile.getInventory().getStackInSlot(tile.getConsumeSlot());
    }
    
    public abstract int willProduceItemID(IConsumeProduceEmptyInTankTile tile);
    
    public boolean addToProduceSlot(IConsumeProduceEmptyInTankTile tile, ItemStack itemStack) {
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
    
}
