package evilcraft.inventory.container;

import evilcraft.core.client.gui.container.GuiWorking;
import evilcraft.core.inventory.container.ContainerWorking;
import evilcraft.core.inventory.slot.SlotSingleItem;
import evilcraft.core.tileentity.WorkingTileEntity;
import evilcraft.tileentity.TileWorking;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * Container for TileWorking instances.
 * @author rubensworks
 */
public class ContainerTileWorking<T extends TileWorking<T>> extends ContainerWorking<T> {

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
            addSlotToContainer(new SlotSingleItem(tile, i, offsetX, offsetY + amount * ITEMBOX, TileWorking.UPGRADE_ITEM));
            amount++;
        }
    }

}
