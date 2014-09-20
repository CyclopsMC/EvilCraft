package evilcraft.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import evilcraft.core.helper.InventoryHelpers;

/**
 * A simple implementation of a crafting grid that stores it's inventory in NBT.
 * @author rubensworks
 *
 */
public class NBTCraftingGrid extends InventoryCrafting {
	
	private static final String NBT_TAG_ROOT = "CraftingGridInventory";
	
	protected NBTTagCompound tag;
	protected EntityPlayer player;
	
	/**
	 * Make a new instance.
	 * @param player The player using the grid.
	 * @param itemStack The item stack to use for storage.
	 * @param eventHandler The event handler if the grid changes.
	 */
	public NBTCraftingGrid(EntityPlayer player, ItemStack itemStack, Container eventHandler) {
		super(eventHandler, 3, 3);
		this.player = player;
		InventoryHelpers.validateNBTStorage(this, itemStack, NBT_TAG_ROOT);
		this.tag = itemStack.getTagCompound();
	}
	
	/**
	 * Save the grid state to NBT.
	 */
	public void save() {
		writeToNBT(tag, NBT_TAG_ROOT);
		player.getCurrentEquippedItem().setTagCompound(tag);
	}
	
	protected void readFromNBT(NBTTagCompound data, String tagName) {
        InventoryHelpers.readFromNBT(this, data, tagName);
    }
	
	protected void writeToNBT(NBTTagCompound data, String tagName) {
        InventoryHelpers.writeToNBT(this, data, tagName);
    }

}
