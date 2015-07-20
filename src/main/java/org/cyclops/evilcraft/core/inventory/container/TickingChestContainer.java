package org.cyclops.evilcraft.core.inventory.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import org.cyclops.evilcraft.core.tileentity.TickingTankInventoryTileEntity;

/**
 * A container for a chest that allows slot ticking.
 * @author rubensworks
 *
 * @param <T> The type of tile.
 */
public abstract class TickingChestContainer<T extends TickingTankInventoryTileEntity<T>> extends TickingTankInventoryContainer<T> {
    
    private int offsetX;
    private int offsetY;
    
    /**
     * Make a new instance.
     * @param inventory The player inventory.
     * @param tile The tile entity this container refers to.
     * @param rows The amount of rows this chest has.
     * @param columns The amount of columns this chest has.
     * @param offsetX The X offset for the chest slots.
     * @param offsetY The Y offset for the chest slots.
     */
    public TickingChestContainer(InventoryPlayer inventory, T tile, int rows, int columns, int offsetX, int offsetY) {
        super(inventory, tile);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.addChestSlots(rows, columns);
    }
    
    protected void addChestSlots(int rows, int columns) {
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                addSlotToContainer(makeSlot(tile, column + row * columns, offsetX + column * 18, offsetY + row * 18));
            }
        }
    }
    
    /**
     * Add a new slot in the given inventory at the given index and at the given row and column.
     * @param inventory The inventory for the chest.
     * @param index The index to make the slot at.
     * @param row The row for the slot.
     * @param column The column for the slot.
     * @return The new slot instance.
     */
    public abstract Slot makeSlot(IInventory inventory, int index, int row, int column);
    
    
    
}
