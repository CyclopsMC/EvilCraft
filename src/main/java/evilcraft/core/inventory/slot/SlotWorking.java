package evilcraft.core.inventory.slot;

import evilcraft.core.tileentity.WorkingTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Slot that is used for only accepting workable items.
 * @author rubensworks
 * @param <T> 
 *
 */
public class SlotWorking<T extends WorkingTileEntity<?, ?>> extends Slot {

	protected T tile;
    private ItemStack lastSlotContents = null;
    
    /**
     * Make a new instance.
     * @param index The index of this slot.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param tile The tile this slot is in.
     */
    public SlotWorking(int index, int x,
            int y, T tile) {
        super(tile, index, x, y);
        this.tile = tile;
        this.lastSlotContents = getStack();
    }
    
    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return itemStack != null && tile.canConsume(itemStack);
    }
    
    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack itemStack) {
        tile.resetWork(true);
    }
    
    @Override
    public void onSlotChanged() {
        if(!ItemStack.areItemStacksEqual(lastSlotContents, this.getStack())) {
            tile.resetWork(true);
        }
        lastSlotContents = this.getStack();
    }
	
}
