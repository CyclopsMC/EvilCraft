package evilcraft.api.gui.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Slot that is used for only accepting one item.
 * @author rubensworks
 *
 */
public class SlotSingleItem extends Slot {
    
	private Item item;
	
    /**
     * Make a new instance.
     * @param inventory The inventory this slot will be in.
     * @param index The index of this slot.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param item The item to accept.
     */
    public SlotSingleItem(IInventory inventory, int index, int x,
            int y, Item item) {
        super(inventory, index, x, y);
        this.item = item;
    }
    
    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return itemStack.getItem() == item;
    }
    
}
