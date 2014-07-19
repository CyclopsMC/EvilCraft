package evilcraft.api.gui.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import evilcraft.api.entities.tileentitites.WorkingTileEntity;

/**
 * Slot that is used for only accepting workable items.
 * @author rubensworks
 * @param <T> 
 *
 */
public class SlotWorking<T extends WorkingTileEntity<?>> extends Slot {

	private T tile;
    
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
        tile.resetWork();
    }
    
    @Override
    public void onSlotChanged() {
    	tile.resetWork();
    }
	
}
