package org.cyclops.evilcraft.core.inventory.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.cyclops.evilcraft.core.tileentity.WorkingTileEntity;

/**
 * Slot that is used for only accepting workable items.
 * @author rubensworks
 * @param <T> The tile type.
 *
 */
public class SlotWorkingRemoveOnly<T extends WorkingTileEntity<?, ?>> extends SlotWorking<T> {
   
	private boolean shouldHardReset;
	
    /**
     * Make a new instance.
     * @param index The index of this slot.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param tile The tile this slot is in.
     * @param shouldHardReset If the tick and required ticks for the tile should be reset.
     */
    public SlotWorkingRemoveOnly(int index, int x,
            int y, T tile, boolean shouldHardReset) {
        super(index, x, y, tile);
        this.shouldHardReset = shouldHardReset;
    }
    
    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return false;
    }
    
    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack itemStack) {
        tile.resetWork(shouldHardReset);
    }
    
    @Override
    public void onSlotChanged() {
    	tile.resetWork(shouldHardReset);
    }
	
}
