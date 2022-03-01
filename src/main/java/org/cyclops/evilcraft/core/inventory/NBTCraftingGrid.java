package org.cyclops.evilcraft.core.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import org.cyclops.cyclopscore.helper.InventoryHelpers;

/**
 * A simple implementation of a crafting grid that stores it's inventory in NBT.
 * @author rubensworks
 *
 */
public class NBTCraftingGrid extends CraftingContainer {

    private static final String NBT_TAG_ROOT = "CraftingGridInventory";

    protected Player player;
    protected int itemIndex;
    protected InteractionHand hand;

    /**
     * Make a new instance.
     * @param player The player using the grid.
     * @param itemIndex The index of the item in the player inventory.
     * @param eventHandler The event handler if the grid changes.
     * @param hand The hand the player is using.
     */
    public NBTCraftingGrid(Player player, int itemIndex, InteractionHand hand, AbstractContainerMenu eventHandler) {
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
