package evilcraft.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import evilcraft.core.helper.MinecraftHelpers;

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
		this.tag = itemStack.getTagCompound();
		this.player = player;
		if(this.tag == null) {
			this.tag = new NBTTagCompound();
			itemStack.setTagCompound(this.tag);
		}
		if(!this.tag.hasKey(NBT_TAG_ROOT)) {
			tag.setTag(NBT_TAG_ROOT, new NBTTagList());
		}
		readFromNBT(tag, NBT_TAG_ROOT);
	}
	
	/**
	 * Save the grid state to NBT.
	 */
	public void save() {
		writeToNBT(tag, NBT_TAG_ROOT);
		player.getCurrentEquippedItem().setTagCompound(tag);
	}
	
	protected void readFromNBT(NBTTagCompound data, String tagName) {
        NBTTagList nbttaglist = data.getTagList(tagName, MinecraftHelpers.NBTTag_Types.NBTTagCompound.ordinal());
        
        for(int j = 0; j < getSizeInventory(); j++) {
        	setInventorySlotContents(j, null);
        }

        for(int j = 0; j < nbttaglist.tagCount(); j++) {
            NBTTagCompound slot = (NBTTagCompound) nbttaglist.getCompoundTagAt(j);
            int index;
            if(slot.hasKey("index")) {
                index = slot.getInteger("index");
            } else {
                index = slot.getByte("Slot");
            }
            if(index >= 0 && index < getSizeInventory()) {
            	setInventorySlotContents(index, ItemStack.loadItemStackFromNBT(slot));
            }
        }
    }
	
	protected void writeToNBT(NBTTagCompound data, String tagName) {
        NBTTagList slots = new NBTTagList();
        for(byte index = 0; index < getSizeInventory(); ++index) {
        	ItemStack itemStack = getStackInSlot(index);
            if(itemStack != null && itemStack.stackSize > 0) {
                NBTTagCompound slot = new NBTTagCompound();
                slot.setInteger("index", index);
                slots.appendTag(slot);
                itemStack.writeToNBT(slot);
            }
        }
        data.setTag(tagName, slots);
    }

}
