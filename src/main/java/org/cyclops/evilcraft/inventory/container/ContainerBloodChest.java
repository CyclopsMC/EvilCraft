package org.cyclops.evilcraft.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import org.cyclops.cyclopscore.inventory.slot.SlotFluidContainer;
import org.cyclops.evilcraft.block.BloodChest;
import org.cyclops.evilcraft.core.inventory.container.TickingChestContainer;
import org.cyclops.evilcraft.inventory.slot.SlotRepairable;
import org.cyclops.evilcraft.tileentity.TileBloodChest;

/**
 * Container for the {@link BloodChest}.
 * @author rubensworks
 *
 */
public class ContainerBloodChest extends TickingChestContainer<TileBloodChest> {
    
    private static final int INVENTORY_OFFSET_X = 28;
    private static final int INVENTORY_OFFSET_Y = 84;
    private static final int ARMOR_INVENTORY_OFFSET_X = 6;
    private static final int ARMOR_INVENTORY_OFFSET_Y = 86;
    
    private static final int CHEST_INVENTORY_OFFSET_X = 100;
    private static final int CHEST_INVENTORY_OFFSET_Y = 27;
    /**
     * Amount of rows in the chest.
     */
    public static final int CHEST_INVENTORY_ROWS = 2;
    /**
     * Amount of columns in the chest.
     */
    public static final int CHEST_INVENTORY_COLUMNS = 5;

    /**
     * Make a new instance.
     * @param inventory The inventory of the player.
     * @param tile The tile entity that calls the GUI.
     */
    public ContainerBloodChest(InventoryPlayer inventory, TileBloodChest tile) {
        super(inventory, tile, CHEST_INVENTORY_ROWS, CHEST_INVENTORY_COLUMNS, CHEST_INVENTORY_OFFSET_X, CHEST_INVENTORY_OFFSET_Y);
        tile.openInventory(inventory.player);
        addSlotToContainer(new SlotFluidContainer(tile, TileBloodChest.SLOT_CONTAINER, 28, 36, tile.getTank())); // Container emptier
        this.addPlayerInventory(inventory, INVENTORY_OFFSET_X, INVENTORY_OFFSET_Y);
        this.addPlayerArmorInventory(inventory, ARMOR_INVENTORY_OFFSET_X, ARMOR_INVENTORY_OFFSET_Y);
    }

    @Override
    public Slot makeSlot(IInventory inventory, int index, int row, int column) {
        return new SlotRepairable(inventory, index, row, column);
    }
    
    @Override
    public void onContainerClosed(EntityPlayer entityplayer) {
        super.onContainerClosed(entityplayer);
        tile.closeInventory(entityplayer);
    }
    
}