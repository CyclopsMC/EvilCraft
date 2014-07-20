package evilcraft.api.gui.slot;

import net.minecraft.item.ItemStack;
import evilcraft.api.entities.tileentitites.WorkingTileEntity;

/**
 * Slot that is used for only accepting workable items.
 * @author rubensworks
 * @param <T> 
 *
 */
public class SlotWorkingRemoveOnly<T extends WorkingTileEntity<?>> extends SlotWorking<T> {
    
    /**
     * Make a new instance.
     * @param index The index of this slot.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param tile The tile this slot is in.
     */
    public SlotWorkingRemoveOnly(int index, int x,
            int y, T tile) {
        super(index, x, y, tile);
    }
    
    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return false;
    }
	
}
