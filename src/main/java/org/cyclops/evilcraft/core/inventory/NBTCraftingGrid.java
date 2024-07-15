package org.cyclops.evilcraft.core.inventory;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import org.cyclops.cyclopscore.inventory.ItemLocation;

/**
 * A simple implementation of a crafting grid that stores it's inventory in NBT.
 * @author rubensworks
 *
 */
public class NBTCraftingGrid extends TransientCraftingContainer {

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
        this.player = player;
        this.itemLocation = itemLocation;

        ItemStack itemStack = itemLocation.getItemStack(player);
        ItemContainerContents contents = itemStack.get(DataComponents.CONTAINER);
        if (contents != null) {
            for (int i = 0; i < contents.getSlots(); i++) {
                setItem(i, contents.getStackInSlot(i));
            }
        }
    }

    /**
     * Save the grid state to NBT.
     */
    public void save() {
        ItemStack itemStack = itemLocation.getItemStack(player);
        itemStack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(this.getItems()));
    }

}
