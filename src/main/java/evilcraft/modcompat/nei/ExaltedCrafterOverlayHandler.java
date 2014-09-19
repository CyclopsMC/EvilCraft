package evilcraft.modcompat.nei;

import codechicken.nei.recipe.DefaultOverlayHandler;

/**
 * An overlay handler for the Exalted Crafter.
 * @author rubensworks
 * TODO: re-implement the canMoveFrom method once NEI is a bit more stable...
 */
@Deprecated
public class ExaltedCrafterOverlayHandler extends DefaultOverlayHandler {

	/*@Override
	public boolean canMoveFrom(Slot slot, GuiContainer gui) {
        return slot.inventory instanceof InventoryPlayer
        		|| slot.inventory instanceof InventoryEnderChest;
    }*/
	
}
