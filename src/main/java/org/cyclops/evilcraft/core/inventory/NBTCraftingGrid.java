package org.cyclops.evilcraft.core.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import org.cyclops.cyclopscore.helper.InventoryHelpers;

/**
 * A simple implementation of a crafting grid that stores it's inventory in NBT.
 * @author rubensworks
 *
 */
public class NBTCraftingGrid extends CraftingInventory {
	
	private static final String NBT_TAG_ROOT = "CraftingGridInventory";
	
	protected PlayerEntity player;
	protected int itemIndex;
	protected Hand hand;
	
	/**
	 * Make a new instance.
	 * @param player The player using the grid.
	 * @param itemIndex The index of the item in the player inventory.
	 * @param eventHandler The event handler if the grid changes.
	 * @param hand The hand the player is using.
	 */
	public NBTCraftingGrid(PlayerEntity player, int itemIndex, Hand hand, Container eventHandler) {
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
		CompoundNBT tag = itemStack.getTag();
        if(tag == null) {
            tag = new CompoundNBT();
        }
		writeToNBT(tag, NBT_TAG_ROOT);
		itemStack.setTag(tag);
	}
	
	protected void readFromNBT(CompoundNBT data, String tagName) {
        InventoryHelpers.readFromNBT(this, data, tagName);
    }
	
	protected void writeToNBT(CompoundNBT data, String tagName) {
        InventoryHelpers.writeToNBT(this, data, tagName);
    }

}
