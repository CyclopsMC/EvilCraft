package evilcraft.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import evilcraft.api.gui.container.TickingChestContainer;
import evilcraft.entities.tileentities.TileBloodChest;
import evilcraft.gui.slot.SlotRepairable;

public class ContainerBloodChest extends TickingChestContainer<TileBloodChest> {
    
    private static final int INVENTORY_OFFSET_X = 8;
    private static final int INVENTORY_OFFSET_Y = 84;
    
    private static final int CHEST_INVENTORY_OFFSET_X = 8;
    private static final int CHEST_INVENTORY_OFFSET_Y = 12;
    public static final int CHEST_INVENTORY_ROWS = 2;
    public static final int CHEST_INVENTORY_COLUMNS = 5;

    public ContainerBloodChest(InventoryPlayer inventory, TileBloodChest tile) {
        super(inventory, tile, CHEST_INVENTORY_ROWS, CHEST_INVENTORY_COLUMNS, CHEST_INVENTORY_OFFSET_X, CHEST_INVENTORY_OFFSET_Y);
        tile.openChest();
        // Adding inventory
        // TODO: with loop
        /*addSlotToContainer(new SlotFluidContainer(tile, TileBloodInfuser.SLOT_CONTAINER, 8, 36)); // Container emptier
        addSlotToContainer(new SlotInfuse(tile, TileBloodInfuser.SLOT_INFUSE, 79, 36, tile)); // Infuse slot
        addSlotToContainer(new SlotRemoveOnly(tile, TileBloodInfuser.SLOT_INFUSE_RESULT, 133, 36)); // Infuse result slot
        */
        this.addPlayerInventory(inventory, INVENTORY_OFFSET_X, INVENTORY_OFFSET_Y);
    }

    @Override
    public Slot makeSlot(IInventory inventory, int index, int row, int column) {
        return new SlotRepairable(inventory, index, row, column);
    }
    
    @Override
    public void onContainerClosed(EntityPlayer entityplayer) {
        super.onContainerClosed(entityplayer);
        tile.closeChest();
    }
    
}