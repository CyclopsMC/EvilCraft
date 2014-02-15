package evilcraft.entities.tileentities.tickaction.bloodinfuser;

import net.minecraft.item.ItemStack;
import evilcraft.api.entities.tileentitites.tickaction.ITickAction;
import evilcraft.entities.tileentities.TileBloodInfuser;

/**
 * Abstract {@link ITickAction} that can infuse items with blood.
 * @author rubensworks
 *
 */
public abstract class BloodInfuserTickAction implements ITickAction<TileBloodInfuser> {
    
    protected final static int MB_PER_TICK = 100;
    
    @Override
    public boolean canTick(TileBloodInfuser tile, ItemStack itemStack, int slot, int tick) {
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
    
    /**
     * Get the stack in the infusion slot.
     * @param tile The tile to check the slot for.
     * @return The item in that slot.
     */
    public ItemStack getInfuseStack(TileBloodInfuser tile) {
        return tile.getInventory().getStackInSlot(tile.getConsumeSlot());
    }
    
    /**
     * Get the item id of the item that will be produced after infusion.
     * @param tile The tile that performs the infusion.
     * @return The item id.
     */
    public abstract int willProduceItemID(TileBloodInfuser tile);
    
    /**
     * Try to add the given item to the production slot.
     * @param tile The tile where infusion happened.
     * @param itemStack The item to try to put in the production slot.
     * @return If the item could be added or joined in the production slot.
     */
    public boolean addToProduceSlot(TileBloodInfuser tile, ItemStack itemStack) {
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
