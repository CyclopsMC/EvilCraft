package evilcraft.api.gui.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import evilcraft.api.entities.tileentitites.TickingTankInventoryTileEntity;

public abstract class TickingChestContainer<T extends TickingTankInventoryTileEntity> extends TickingInventoryContainer<T> {
    
    private int offsetX;
    private int offsetY;
    
    public TickingChestContainer(InventoryPlayer inventory, T tile, int rows, int columns, int offsetX, int offsetY) {
        super(inventory, tile);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.addChestSlots(rows, columns);
    }
    
    protected void addChestSlots(int rows, int columns) {
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                addSlotToContainer(makeSlot(tile, column + row * rows, offsetX + column * 18, offsetY + row * 18));
            }
        }
    }
    
    public abstract Slot makeSlot(IInventory inventory, int index, int row, int column);
    
    
    
}
