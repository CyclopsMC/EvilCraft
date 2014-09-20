package evilcraft.core.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import evilcraft.core.item.ItemGui;

/**
 * A container for an item.
 * @author rubensworks
 *
 * @param <I> The item instance.
 */
public abstract class ItemInventoryContainer<I extends ItemGui> extends ExtendedInventoryContainer {
	
	protected I item;

	/**
	 * Make a new instance.
	 * @param inventory The player inventory.
	 * @param item The item.
	 */
	public ItemInventoryContainer(InventoryPlayer inventory, I item) {
		super(inventory, item);
		this.item = item;
	}

	/**
	 * Get the item instance.
	 * @return The item.
	 */
	public I getItem() {
		return item;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return player.getHeldItem() != null && player.getHeldItem().getItem() == getItem();
	}
	
	@Override
	protected Slot createNewSlot(IInventory inventory, int index, int x, int y) {
    	return new Slot(inventory, index, x, y) {
    		
    		@Override
    		public boolean canTakeStack(EntityPlayer player) {
    			return this.getStack() != player.getCurrentEquippedItem();
    	    }
    		
    	};
    }
	
}
