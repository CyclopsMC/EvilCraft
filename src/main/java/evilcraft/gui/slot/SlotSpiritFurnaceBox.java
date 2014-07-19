package evilcraft.gui.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import evilcraft.api.gui.slot.SlotSingleItem;
import evilcraft.entities.tileentities.TileSpiritFurnace;

/**
 * Slot that is used for accepting boxes in spirit furnaces.
 * @author rubensworks
 *
 */
public class SlotSpiritFurnaceBox extends SlotSingleItem {

	private TileSpiritFurnace tile;
	
	/**
     * Make a new instance.
     * @param inventory The inventory this slot will be in.
     * @param index The index of this slot.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param item The item to accept.
	 * @param tile The tile this slot is in.
     */
	public SlotSpiritFurnaceBox(IInventory inventory, int index, int x, int y,
			Item item, TileSpiritFurnace tile) {
		super(inventory, index, x, y, item);
		this.tile = tile;
	}
	
	@Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack itemStack) {
        tile.resetCooking();
    }

}
