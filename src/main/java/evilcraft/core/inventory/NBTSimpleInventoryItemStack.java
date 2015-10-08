package evilcraft.core.inventory;

import evilcraft.core.helper.InventoryHelpers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * A simple inventory for an ItemStack that can be stored in NBT.
 * @author rubensworks
 *
 */
public class NBTSimpleInventoryItemStack extends SimpleInventory {

	private static final String NBT_TAG_ROOT = NBTSimpleInventoryItemHeld.NBT_TAG_ROOT;

	protected ItemStack itemStack;

	/**
     * Make a new instance.
	 * @param itemStack The item stack.
     * @param size The amount of slots in the inventory.
     * @param stackLimit The stack limit for each slot.
     */
	public NBTSimpleInventoryItemStack(ItemStack itemStack, int size, int stackLimit) {
		super(size, NBT_TAG_ROOT, stackLimit);
		this.itemStack = itemStack;
		InventoryHelpers.validateNBTStorage(this, itemStack, NBT_TAG_ROOT);
	}
	
	@Override
	public void markDirty() {
		NBTTagCompound tag = itemStack.getTagCompound();
		if(tag == null) {
			tag = new NBTTagCompound();
			itemStack.setTagCompound(tag);
		}
		writeToNBT(tag, NBT_TAG_ROOT);
		itemStack.setTagCompound(tag);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound data, String tagName) {
        InventoryHelpers.readFromNBT(this, data, tagName);
    }
	
	@Override
	public void writeToNBT(NBTTagCompound data, String tagName) {
        InventoryHelpers.writeToNBT(this, data, tagName);
    }

}
