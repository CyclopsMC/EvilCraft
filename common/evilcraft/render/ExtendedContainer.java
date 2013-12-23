package evilcraft.render;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class ExtendedContainer extends Container{
    
    protected static final int ITEMBOX = 18;
    
    private int inventorySize;

    public ExtendedContainer(int inventorySize) {
        this.inventorySize = inventorySize;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return true;
    }
    
    /**
     * Add player inventory and hotbar to the GUI.
     * @param inventory Inventory of the player
     * @param offsetX Offset to X
     * @param offsetY Offset to Y
     */
    protected void addPlayerInventory(InventoryPlayer inventory, int offsetX, int offsetY) {
        // Player inventory
        int rows = 3;
        int cols = 9;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                // Slot params: id, x-coord, y-coord (coords are relative to gui box)
                addSlotToContainer(new Slot(inventory, x + (y + 1) * cols, offsetX + x * ITEMBOX, offsetY + y * ITEMBOX));
            }
        }
        
        // Player hotbar
        offsetY += 58;
        for (int x = 0; x < cols; x++) {
            addSlotToContainer(new Slot(inventory, x, offsetX + x * ITEMBOX, offsetY));
        }
    }
    
}
