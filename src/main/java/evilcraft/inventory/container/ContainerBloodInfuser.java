package evilcraft.inventory.container;

import evilcraft.block.BloodInfuser;
import evilcraft.core.inventory.slot.SlotFluidContainer;
import evilcraft.core.inventory.slot.SlotRemoveOnly;
import evilcraft.core.inventory.slot.SlotWorking;
import evilcraft.tileentity.TileBloodInfuser;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * Container for the {@link BloodInfuser}.
 * @author rubensworks
 *
 */
public class ContainerBloodInfuser extends ContainerTileWorking<TileBloodInfuser> {
    
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
     * Infuse slot X coordinate.
     */
    public static final int SLOT_INFUSE_X = 79;
    /**
     * Infuse slot Y coordinate.
     */
    public static final int SLOT_INFUSE_Y = 36;
    
    /**
     * Infuse result slot X coordinate.
     */
    public static final int SLOT_INFUSE_RESULT_X = 133;
    /**
     * Infuse result slot Y coordinate.
     */
    public static final int SLOT_INFUSE_RESULT_Y = 36;

    /**
     * Make a new instance.
     * @param inventory The inventory of the player.
     * @param tile The tile entity that calls the GUI.
     */
    public ContainerBloodInfuser(InventoryPlayer inventory, TileBloodInfuser tile) {
        super(inventory, tile);

        // Adding inventory
        addSlotToContainer(new SlotFluidContainer(tile, TileBloodInfuser.SLOT_CONTAINER, SLOT_CONTAINER_X, SLOT_CONTAINER_Y, tile.getTank())); // Container emptier
        addSlotToContainer(new SlotWorking<TileBloodInfuser>(TileBloodInfuser.SLOT_INFUSE, SLOT_INFUSE_X, SLOT_INFUSE_Y, tile)); // Infuse slot
        addSlotToContainer(new SlotRemoveOnly(tile, TileBloodInfuser.SLOT_INFUSE_RESULT, SLOT_INFUSE_RESULT_X, SLOT_INFUSE_RESULT_Y)); // Infuse result slot

        this.addUpgradeInventory(INVENTORY_OFFSET_X, 0); // TODO: temporary positioning

        this.addPlayerInventory(inventory, INVENTORY_OFFSET_X, INVENTORY_OFFSET_Y);
    }
    
}