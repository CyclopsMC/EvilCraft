package org.cyclops.evilcraft.core.inventory.container;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import org.cyclops.evilcraft.core.blockentity.BlockEntityTickingTankInventory;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * A container for a chest that allows slot ticking.
 * @author rubensworks
 *
 * @param <T> The type of tile.
 */
public abstract class ContainerTickingChest<T extends BlockEntityTickingTankInventory<T>> extends ContainerInventoryTickingTank<T> {

    private int offsetX;
    private int offsetY;

    public ContainerTickingChest(@Nullable MenuType<?> type, int id, Inventory playerInventory,
                                         Container inventory, Optional<T> tileSupplier, int tickers,
                                 int rows, int columns, int offsetX, int offsetY) {
        super(type, id, playerInventory, inventory, tileSupplier, tickers);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.addChestSlots(rows, columns);
    }

    protected void addChestSlots(int rows, int columns) {
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                addSlot(makeSlot(inventory, column + row * columns, offsetX + column * 18, offsetY + row * 18));
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
    public abstract Slot makeSlot(Container inventory, int index, int row, int column);



}
