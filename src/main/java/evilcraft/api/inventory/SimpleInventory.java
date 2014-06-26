package evilcraft.api.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import evilcraft.api.Helpers;

/**
 * A basic inventory implementation.
 * @author rubensworks
 *
 */
public class SimpleInventory implements IInventory {

    private final ItemStack[] _contents;
    private final String _name;
    private final int _stackLimit;

    /**
     * Make a new instance.
     * @param size The amount of slots in the inventory.
     * @param name The name of the inventory, used for NBT storage.
     * @param stackLimit The stack limit for each slot.
     */
    public SimpleInventory(int size, String name, int stackLimit) {
        _contents = new ItemStack[size];
        _name = name;
        _stackLimit = stackLimit;
    }

    @Override
    public int getSizeInventory() {
        return _contents.length;
    }

    @Override
    public ItemStack getStackInSlot(int slotId) {
        return _contents[slotId];
    }

    @Override
    public ItemStack decrStackSize(int slotId, int count) {
        if (slotId < _contents.length && _contents[slotId] != null) {
            if (_contents[slotId].stackSize > count) {
                ItemStack result = _contents[slotId].splitStack(count);
                onInventoryChanged();
                return result;
            }
            ItemStack stack = _contents[slotId];
            _contents[slotId] = null;
            onInventoryChanged();
            return stack;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int slotId, ItemStack itemstack) {
        if (slotId >= _contents.length) {
            return;
        }
        this._contents[slotId] = itemstack;

        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
            itemstack.stackSize = this.getInventoryStackLimit();
        }
        onInventoryChanged();
    }

    @Override
    public String getInventoryName() {
        return _name;
    }

    @Override
    public int getInventoryStackLimit() {
        return _stackLimit;
    }

    private void onInventoryChanged() {
        markDirty();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    /**
     * Read inventory data from the given NBT.
     * @param data The NBT data containing inventory data.
     */
    public void readFromNBT(NBTTagCompound data) {
        readFromNBT(data, "items");
    }

    /**
     * Read inventory data from the given NBT.
     * @param data The NBT data containing inventory data.
     * @param tag The NBT tag name where the info is located.
     */
    public void readFromNBT(NBTTagCompound data, String tag) {
        NBTTagList nbttaglist = data.getTagList(tag, Helpers.NBTTag_Types.NBTTagCompound.ordinal());
        
        for (int j = 0; j < _contents.length; ++j)
            _contents[j] = null;

        for (int j = 0; j < nbttaglist.tagCount(); ++j) {
            NBTTagCompound slot = (NBTTagCompound) nbttaglist.getCompoundTagAt(j);
            int index;
            if (slot.hasKey("index")) {
                index = slot.getInteger("index");
            } else {
                index = slot.getByte("Slot");
            }
            if (index >= 0 && index < _contents.length) {
                _contents[index] = ItemStack.loadItemStackFromNBT(slot);
            }
        }
    }

    /**
     * Write inventory data to the given NBT.
     * @param data The NBT tag that will receive inventory data.
     */
    public void writeToNBT(NBTTagCompound data) {
        writeToNBT(data, "items");
    }

    /**
     * Write inventory data to the given NBT.
     * @param data The NBT tag that will receive inventory data.
     * @param tag The NBT tag name where the info must be located.
     */
    public void writeToNBT(NBTTagCompound data, String tag) {
        NBTTagList slots = new NBTTagList();
        for (byte index = 0; index < _contents.length; ++index) {
            if (_contents[index] != null && _contents[index].stackSize > 0) {
                NBTTagCompound slot = new NBTTagCompound();
                slots.appendTag(slot);
                slot.setByte("Slot", index);
                _contents[index].writeToNBT(slot);
            }
        }
        data.setTag(tag, slots);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slotId) {
        if (this._contents[slotId] == null) {
            return null;
        }

        ItemStack stackToTake = this._contents[slotId];
        this._contents[slotId] = null;
        return stackToTake;
    }

    /**
     * Get the array of {@link ItemStack} inside this inventory.
     * @return The items in this inventory.
     */
    public ItemStack[] getItemStacks() {
        return _contents;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

	@Override
	public void markDirty() {
		
	}
}