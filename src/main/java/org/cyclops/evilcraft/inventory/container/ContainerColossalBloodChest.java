package org.cyclops.evilcraft.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import org.cyclops.cyclopscore.inventory.slot.SlotFluidContainer;
import org.cyclops.evilcraft.block.ColossalBloodChest;
import org.cyclops.evilcraft.inventory.slot.SlotRepairable;
import org.cyclops.evilcraft.tileentity.TileColossalBloodChest;

/**
 * Container for the {@link ColossalBloodChest}.
 * @author rubensworks
 *
 */
public class ContainerColossalBloodChest extends ContainerTileWorking<TileColossalBloodChest> {

    private static final int INVENTORY_OFFSET_X = 28;
    private static final int INVENTORY_OFFSET_Y = 107;
    private static final int ARMOR_INVENTORY_OFFSET_X = 192;
    private static final int ARMOR_INVENTORY_OFFSET_Y = 109;

    private static final int CHEST_INVENTORY_OFFSET_X = 59;
    private static final int CHEST_INVENTORY_OFFSET_Y = 13;
    /**
     * Amount of rows in the chest.
     */
    public static final int CHEST_INVENTORY_ROWS = 5;
    /**
     * Amount of columns in the chest.
     */
    public static final int CHEST_INVENTORY_COLUMNS = 9;

    /**
     * Container slot X coordinate.
     */
    public static final int SLOT_CONTAINER_X = 6;
    /**
     * Container slot Y coordinate.
     */
    public static final int SLOT_CONTAINER_Y = 46;

    private static final int UPGRADE_INVENTORY_OFFSET_X = -22;
    private static final int UPGRADE_INVENTORY_OFFSET_Y = 6;

    /**
     * Make a new instance.
     * @param inventory The inventory of the player.
     * @param tile The tile entity that calls the GUI.
     */
    public ContainerColossalBloodChest(InventoryPlayer inventory, TileColossalBloodChest tile) {
        super(inventory, tile);

        tile.openInventory(inventory.player);

        // Adding inventory
        addSlotToContainer(new SlotFluidContainer(tile, TileColossalBloodChest.SLOT_CONTAINER,
        		SLOT_CONTAINER_X, SLOT_CONTAINER_Y,
        		tile.getTank())); // Container emptier
        
        addChestSlots(CHEST_INVENTORY_ROWS, CHEST_INVENTORY_COLUMNS);

        this.addUpgradeInventory(UPGRADE_INVENTORY_OFFSET_X, UPGRADE_INVENTORY_OFFSET_Y);

        this.addPlayerInventory(inventory, INVENTORY_OFFSET_X, INVENTORY_OFFSET_Y);
        this.addPlayerArmorInventory(inventory, ARMOR_INVENTORY_OFFSET_X, ARMOR_INVENTORY_OFFSET_Y);
    }

    protected void addChestSlots(int rows, int columns) {
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                addSlotToContainer(makeSlot(tile, column + row * columns, CHEST_INVENTORY_OFFSET_X + column * 18, CHEST_INVENTORY_OFFSET_Y + row * 18));
            }
        }
    }

    protected Slot makeSlot(IInventory inventory, int index, int row, int column) {
        return new SlotRepairable(inventory, index, row, column);
    }

    @Override
    public void onContainerClosed(EntityPlayer entityplayer) {
        super.onContainerClosed(entityplayer);
        tile.closeInventory(entityplayer);
    }
    
}