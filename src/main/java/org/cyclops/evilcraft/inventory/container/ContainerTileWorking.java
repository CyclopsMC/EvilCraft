package org.cyclops.evilcraft.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.inventory.slot.SlotSingleItem;
import org.cyclops.evilcraft.core.client.gui.container.GuiWorking;
import org.cyclops.evilcraft.core.inventory.container.ContainerWorking;
import org.cyclops.evilcraft.core.tileentity.WorkingTileEntity;
import org.cyclops.evilcraft.tileentity.TileWorking;

/**
 * Container for TileWorking instances.
 * @author rubensworks
 */
public class ContainerTileWorking<T extends TileWorking<T, ?>> extends ContainerWorking<T> {

    /**
     * Make a new instance.
     * @param inventory The inventory of the player.
     * @param tile The tile entity that calls the GUI.
     */
    public ContainerTileWorking(InventoryPlayer inventory, T tile) {
        super(inventory, tile);
        this.offsetX = GuiWorking.UPGRADES_OFFSET_X;
    }

    public void addUpgradeInventory(int offsetX, int offsetY) {
        int upgradeSlots = WorkingTileEntity.INVENTORY_SIZE_UPGRADES;
        int amount = 0;
        for(int i = tile.getBasicInventorySize(); i < tile.getBasicInventorySize() + upgradeSlots; i++) {
            addSlotToContainer(new SlotSingleItem(tile, i, offsetX, offsetY + amount * ITEMBOX, TileWorking.UPGRADE_ITEM) {

                private ItemStack lastSlotContents = getStack();

                @Override
                public boolean isItemValid(ItemStack itemStack) {
                    return super.isItemValid(itemStack) && tile.canInsertItem(getSlotIndex(), itemStack);
                }

                @Override
                public boolean canTakeStack(EntityPlayer player) {
                    return super.canTakeStack(player) &&
                            tile.canExtractItem(getSlotIndex(), getStack(), player.inventory.getItemStack());
                }

                @Override
                public void onSlotChanged() {
                    if(!ItemStack.areItemStacksEqual(lastSlotContents, this.getStack())) {
                        tile.onUpgradeSlotChanged(getSlotIndex(),lastSlotContents, this.getStack());
                    }
                    lastSlotContents = this.getStack();
                    if(lastSlotContents != null) lastSlotContents = lastSlotContents.copy();
                }

            });
            amount++;
        }
    }

}
