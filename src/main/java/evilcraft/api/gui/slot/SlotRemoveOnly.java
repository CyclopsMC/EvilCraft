package evilcraft.api.gui.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * A slot from which the player can only remove an {@link ItemStack}, not place one inside it.
 * @author rubensworks
 *
 */
public class SlotRemoveOnly extends Slot {

    /**
     * Make a new instance.
     * @param inventory The inventory for which the slot applies.
     * @param index The index the slot is at.
     * @param x The X coordinate for the slot to render at.
     * @param y The Y coordinate for the slot to render at.
     */
    public SlotRemoveOnly(IInventory inventory, int index, int x,
            int y) {
        super(inventory, index, x, y);
    }
    
    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return false;
    }

}
