package evilcraft.core.inventory.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import evilcraft.core.tileentity.WorkingTileEntity;

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
        if(lastSlotContents == null || this.getStack() == null || lastSlotContents.getItem() != this.getStack().getItem()) {
            tile.resetWork(true);
        }
        lastSlotContents = this.getStack();
    }
	
}
