package org.cyclops.evilcraft.core.inventory.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.cyclops.evilcraft.core.tileentity.WorkingTileEntity;

/**
 * Slot that is used for only accepting workable items.
 * @author rubensworks
 * @param <T> The tile type.
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
        if(!ItemStack.areItemStackTagsEqual(itemStack, this.getStack())) {
            tile.resetWork();
        }
    }
    
    @Override
    public void onSlotChanged() {
        if(!ItemStack.areItemStackTagsEqual(lastSlotContents, this.getStack())) {
            tile.resetWork();
        }
        lastSlotContents = this.getStack();
        if(lastSlotContents != null) lastSlotContents = lastSlotContents.copy();
    }
	
}
