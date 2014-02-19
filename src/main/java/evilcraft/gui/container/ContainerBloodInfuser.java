package evilcraft.gui.container;

import net.minecraft.entity.player.InventoryPlayer;
import evilcraft.api.gui.container.TickingTankInventoryContainer;
import evilcraft.api.gui.slot.SlotRemoveOnly;
import evilcraft.blocks.BloodInfuser;
import evilcraft.entities.tileentities.TileBloodInfuser;
import evilcraft.gui.slot.SlotFluidContainer;
import evilcraft.gui.slot.SlotInfuse;

/**
 * Container for the {@link BloodInfuser}.
 * @author rubensworks
 *
 */
public class ContainerBloodInfuser extends TickingTankInventoryContainer<TileBloodInfuser> {
    
    private static final int INVENTORY_OFFSET_X = 8;
    private static final int INVENTORY_OFFSET_Y = 84;

    /**
     * Make a new instance.
     * @param inventory The inventory of the player.
     * @param tile The tile entity that calls the GUI.
     */
    public ContainerBloodInfuser(InventoryPlayer inventory, TileBloodInfuser tile) {
        super(inventory, tile);

        // Adding inventory
        addSlotToContainer(new SlotFluidContainer(tile, TileBloodInfuser.SLOT_CONTAINER, 8, 36, TileBloodInfuser.ACCEPTED_FLUID)); // Container emptier
        addSlotToContainer(new SlotInfuse(tile, TileBloodInfuser.SLOT_INFUSE, 79, 36, tile)); // Infuse slot
        addSlotToContainer(new SlotRemoveOnly(tile, TileBloodInfuser.SLOT_INFUSE_RESULT, 133, 36)); // Infuse result slot

        this.addPlayerInventory(inventory, INVENTORY_OFFSET_X, INVENTORY_OFFSET_Y);
    }
    
}