package org.cyclops.evilcraft.core.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.helper.InventoryHelpers;
import org.cyclops.cyclopscore.inventory.ItemLocation;

/**
 * A simple implementation of a crafting grid that stores it's inventory in NBT.
 * @author rubensworks
 *
 */
public class NBTCraftingGrid extends TransientCraftingContainer {

    private static final String NBT_TAG_ROOT = "CraftingGridInventory";

    protected Player player;
    protected ItemLocation itemLocation;

    /**
     * Make a new instance.
     * @param player The player using the grid.
     * @param itemLocation The item location.
     * @param eventHandler The event handler if the grid changes.
     */
    public NBTCraftingGrid(Player player, ItemLocation itemLocation, AbstractContainerMenu eventHandler) {
        super(eventHandler, 3, 3);
        ItemStack itemStack = itemLocation.getItemStack(player);
        this.player = player;
        this.itemLocation = itemLocation;
        InventoryHelpers.validateNBTStorage(this, itemStack, NBT_TAG_ROOT);
    }

    /**
     * Save the grid state to NBT.
     */
    public void save() {
        ItemStack itemStack = itemLocation.getItemStack(player);
        CompoundTag tag = itemStack.getTag();
        if(tag == null) {
            tag = new CompoundTag();
        }
        writeToNBT(tag, NBT_TAG_ROOT);
        itemStack.setTag(tag);
    }

    protected void readFromNBT(CompoundTag data, String tagName) {
        InventoryHelpers.readFromNBT(this, data, tagName);
    }

    protected void writeToNBT(CompoundTag data, String tagName) {
        InventoryHelpers.writeToNBT(this, data, tagName);
    }

}
