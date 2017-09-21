package org.cyclops.evilcraft.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import org.cyclops.cyclopscore.helper.InventoryHelpers;

/**
 * A simple implementation of a crafting grid that stores it's inventory in NBT.
 * @author rubensworks
 *
 */
public class NBTCraftingGrid extends InventoryCrafting {
	
	private static final String NBT_TAG_ROOT = "CraftingGridInventory";
	
	protected EntityPlayer player;
	protected int itemIndex;
	protected EnumHand hand;
	
	/**
	 * Make a new instance.
	 * @param player The player using the grid.
	 * @param itemIndex The index of the item in the player inventory.
	 * @param eventHandler The event handler if the grid changes.
	 * @param hand The hand the player is using.
	 */
	public NBTCraftingGrid(EntityPlayer player, int itemIndex, EnumHand hand, Container eventHandler) {
		super(eventHandler, 3, 3);
		ItemStack itemStack = InventoryHelpers.getItemFromIndex(player, itemIndex, hand);
		this.player = player;
		this.itemIndex = itemIndex;
		this.hand = hand;
		InventoryHelpers.validateNBTStorage(this, itemStack, NBT_TAG_ROOT);
	}
	
	/**
	 * Save the grid state to NBT.
	 */
	public void save() {
        ItemStack itemStack = InventoryHelpers.getItemFromIndex(player, itemIndex, hand);
		NBTTagCompound tag = itemStack.getTagCompound();
        if(tag == null) {
            tag = new NBTTagCompound();
        }
		writeToNBT(tag, NBT_TAG_ROOT);
		itemStack.setTagCompound(tag);
	}
	
	protected void readFromNBT(NBTTagCompound data, String tagName) {
        InventoryHelpers.readFromNBT(this, data, tagName);
    }
	
	protected void writeToNBT(NBTTagCompound data, String tagName) {
        InventoryHelpers.writeToNBT(this, data, tagName);
    }

}
