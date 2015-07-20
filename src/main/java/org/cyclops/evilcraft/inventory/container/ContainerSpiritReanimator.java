package org.cyclops.evilcraft.inventory.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import org.cyclops.cyclopscore.inventory.slot.SlotFluidContainer;
import org.cyclops.cyclopscore.inventory.slot.SlotRemoveOnly;
import org.cyclops.cyclopscore.inventory.slot.SlotSingleItem;
import org.cyclops.evilcraft.block.SpiritReanimator;
import org.cyclops.evilcraft.core.inventory.slot.SlotWorking;
import org.cyclops.evilcraft.tileentity.TileSpiritReanimator;

/**
 * Container for the {@link SpiritReanimator}.
 * @author rubensworks
 *
 */
public class ContainerSpiritReanimator extends ContainerTileWorking<TileSpiritReanimator> {
    
    private static final int INVENTORY_OFFSET_X = 8;
    private static final int INVENTORY_OFFSET_Y = 84;
    
    /**
     * Container slot X coordinate.
     */
    public static final int SLOT_CONTAINER_X = 8;
    /**
     * Container slot Y coordinate.
     */
    public static final int SLOT_CONTAINER_Y = 36;
    
    /**
     * Box slot X coordinate.
     */
    public static final int SLOT_BOX_X = 97;
    /**
     * Box slot Y coordinate.
     */
    public static final int SLOT_BOX_Y = 22;
    
    /**
     * Egg slot X coordinate.
     */
    public static final int SLOT_EGG_X = 135;
    /**
     * Egg slot Y coordinate.
     */
    public static final int SLOT_EGG_Y = 22;
    
    /**
     * Output slot X coordinate.
     */
    public static final int SLOT_OUTPUT_X = 116;
    /**
     * Output slot Y coordinate.
     */
    public static final int SLOT_OUTPUT_Y = 52;

    private static final int UPGRADE_INVENTORY_OFFSET_X = -22;
    private static final int UPGRADE_INVENTORY_OFFSET_Y = 6;

    /**
     * Make a new instance.
     * @param inventory The inventory of the player.
     * @param tile The tile entity that calls the GUI.
     */
    public ContainerSpiritReanimator(InventoryPlayer inventory, TileSpiritReanimator tile) {
        super(inventory, tile);

        // Adding inventory
        addSlotToContainer(new SlotFluidContainer(tile, TileSpiritReanimator.SLOT_CONTAINER,
        		SLOT_CONTAINER_X, SLOT_CONTAINER_Y,
        		tile.getTank())); // Container emptier
        addSlotToContainer(new SlotWorking<TileSpiritReanimator>(TileSpiritReanimator.SLOT_BOX, SLOT_BOX_X, SLOT_BOX_Y, tile)); // Box slot
        addSlotToContainer(new SlotSingleItem(tile, TileSpiritReanimator.SLOT_EGG, SLOT_EGG_X, SLOT_EGG_Y, Items.egg));
        addSlotToContainer(new SlotRemoveOnly(tile, TileSpiritReanimator.SLOTS_OUTPUT, SLOT_OUTPUT_X, SLOT_OUTPUT_Y));

        this.addUpgradeInventory(UPGRADE_INVENTORY_OFFSET_X, UPGRADE_INVENTORY_OFFSET_Y);

        this.addPlayerInventory(inventory, INVENTORY_OFFSET_X, INVENTORY_OFFSET_Y);
    }
    
}