package org.cyclops.evilcraft.inventory.container;

import org.cyclops.cyclopscore.inventory.slot.SlotRemoveOnly;
import org.cyclops.evilcraft.core.inventory.slot.SlotWorking;
import org.cyclops.evilcraft.tileentity.TileSanguinaryEnvironmentalAccumulator;
import net.minecraft.entity.player.InventoryPlayer;
import org.cyclops.evilcraft.block.SanguinaryEnvironmentalAccumulator;

/**
 * Container for the {@link SanguinaryEnvironmentalAccumulator}.
 * @author rubensworks
 *
 */
public class ContainerSanguinaryEnvironmentalAccumulator extends ContainerTileWorking<TileSanguinaryEnvironmentalAccumulator> {

    private static final int INVENTORY_OFFSET_X = 8;
    private static final int INVENTORY_OFFSET_Y = 84;

    /**
     * Accumulate slot X coordinate.
     */
    public static final int SLOT_ACCUMULATE_X = 54;
    /**
     * Accumulate slot Y coordinate.
     */
    public static final int SLOT_ACCUMULATE_Y = 36;

    /**
     * Accumulate result slot X coordinate.
     */
    public static final int SLOT_ACCUMULATE_RESULT_X = 108;
    /**
     * Accumulate result slot Y coordinate.
     */
    public static final int SLOT_ACCUMULATE_RESULT_Y = 36;

    private static final int UPGRADE_INVENTORY_OFFSET_X = -22;
    private static final int UPGRADE_INVENTORY_OFFSET_Y = 6;

    /**
     * Make a new instance.
     * @param inventory The inventory of the player.
     * @param tile The tile entity that calls the GUI.
     */
    public ContainerSanguinaryEnvironmentalAccumulator(InventoryPlayer inventory, TileSanguinaryEnvironmentalAccumulator tile) {
        super(inventory, tile);

        // Adding inventory
        addSlotToContainer(new SlotWorking<TileSanguinaryEnvironmentalAccumulator>(TileSanguinaryEnvironmentalAccumulator.SLOT_ACCUMULATE, SLOT_ACCUMULATE_X, SLOT_ACCUMULATE_Y, tile)); // Accumulate slot
        addSlotToContainer(new SlotRemoveOnly(tile, TileSanguinaryEnvironmentalAccumulator.SLOT_ACCUMULATE_RESULT, SLOT_ACCUMULATE_RESULT_X, SLOT_ACCUMULATE_RESULT_Y)); // Accumulate result slot

        this.addUpgradeInventory(UPGRADE_INVENTORY_OFFSET_X, UPGRADE_INVENTORY_OFFSET_Y);

        this.addPlayerInventory(inventory, INVENTORY_OFFSET_X, INVENTORY_OFFSET_Y);
    }
    
}