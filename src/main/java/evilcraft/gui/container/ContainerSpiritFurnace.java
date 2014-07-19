package evilcraft.gui.container;

import net.minecraft.entity.player.InventoryPlayer;
import evilcraft.api.gui.container.TickingTankInventoryContainer;
import evilcraft.api.gui.slot.SlotRemoveOnly;
import evilcraft.blocks.SpiritFurnace;
import evilcraft.entities.tileentities.TileSpiritFurnace;
import evilcraft.gui.slot.SlotFluidContainer;
import evilcraft.gui.slot.SlotSingleItem;

/**
 * Container for the {@link SpiritFurnace}.
 * @author rubensworks
 *
 */
public class ContainerSpiritFurnace extends TickingTankInventoryContainer<TileSpiritFurnace> {
    
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
    public static final int SLOT_BOX_X = 79;
    /**
     * Box slot Y coordinate.
     */
    public static final int SLOT_BOX_Y = 36;
    
    /**
     * Drop slot X coordinate.
     */
    public static final int SLOT_DROP_X = 133;
    /**
     * Drop slot Y coordinate.
     */
    public static final int SLOT_DROP_Y = 36;

    /**
     * Make a new instance.
     * @param inventory The inventory of the player.
     * @param tile The tile entity that calls the GUI.
     */
    public ContainerSpiritFurnace(InventoryPlayer inventory, TileSpiritFurnace tile) {
        super(inventory, tile);

        // Adding inventory
        addSlotToContainer(new SlotFluidContainer(tile, TileSpiritFurnace.SLOT_CONTAINER, SLOT_CONTAINER_X, SLOT_CONTAINER_Y, TileSpiritFurnace.ACCEPTED_FLUID)); // Container emptier
        addSlotToContainer(new SlotSingleItem(tile, TileSpiritFurnace.SLOT_BOX, SLOT_BOX_X, SLOT_BOX_Y, TileSpiritFurnace.getAllowedCookItem())); // Box slot
        addSlotToContainer(new SlotRemoveOnly(tile, TileSpiritFurnace.SLOT_DROP, SLOT_DROP_X, SLOT_DROP_Y)); // Drop slot

        this.addPlayerInventory(inventory, INVENTORY_OFFSET_X, INVENTORY_OFFSET_Y);
    }
    
}