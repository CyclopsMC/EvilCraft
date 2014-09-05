package evilcraft.core.helpers;

import net.minecraft.inventory.IInventory;

/**
 * Contains helper methods involving {@link IInventory}S.
 * @author immortaleeb
 *
 */
public class InventorHelpers {

	/**
	 * Erase a complete inventory
	 * @param inventory inventory to clear
	 */
	public static void clearInventory(IInventory inventory) {
	    for (int i = 0; i < inventory.getSizeInventory(); i++) {
	        inventory.setInventorySlotContents(i, null);
	    }
	}

}
