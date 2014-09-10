package evilcraft.core.helper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * Contains helper methods involving {@link IInventory}S.
 * @author immortaleeb
 *
 */
public class InventoryHelpers {

	/**
	 * Erase a complete inventory
	 * @param inventory inventory to clear
	 */
	public static void clearInventory(IInventory inventory) {
	    for (int i = 0; i < inventory.getSizeInventory(); i++) {
	        inventory.setInventorySlotContents(i, null);
	    }
	}
	
	/**
	 * Try adding a new item stack originating from the given original stack to the same original stack.
	 * The original item stack should not have it's stack-size decreased yet, this method does this.
	 * Otherwise it will add the new stack to another inventory slot and in the worst case drop it on the floor.
	 * @param player The player.
	 * @param originalStack The original item stack from which the new item stack originated.
	 * @param newStackPart The new item stack.
	 */
	public static void tryReAddToStack(EntityPlayer player, ItemStack originalStack, ItemStack newStackPart) {
		if (!player.capabilities.isCreativeMode) {
        	if(--originalStack.stackSize == 0) {
        		player.inventory.setInventorySlotContents(player.inventory.currentItem, newStackPart);
        	} else {
        		if(!player.inventory.addItemStackToInventory(newStackPart)) {
        			player.dropPlayerItemWithRandomChoice(newStackPart, false);
        		}
        	}
        }
	}

}
