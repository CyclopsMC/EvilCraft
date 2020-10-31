package org.cyclops.evilcraft.inventory.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import org.cyclops.cyclopscore.inventory.slot.SlotFluidContainer;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockBloodChest;
import org.cyclops.evilcraft.core.inventory.container.ContainerTickingChest;
import org.cyclops.evilcraft.inventory.slot.SlotRepairable;
import org.cyclops.evilcraft.tileentity.TileBloodChest;

import java.util.Optional;

/**
 * Container for the {@link BlockBloodChest}.
 * @author rubensworks
 *
 */
public class ContainerBloodChest extends ContainerTickingChest<TileBloodChest> {
    
    private static final int INVENTORY_OFFSET_X = 28;
    private static final int INVENTORY_OFFSET_Y = 84;
    private static final int ARMOR_INVENTORY_OFFSET_X = 6;
    private static final int ARMOR_INVENTORY_OFFSET_Y = 86;
    
    private static final int CHEST_INVENTORY_OFFSET_X = 100;
    private static final int CHEST_INVENTORY_OFFSET_Y = 27;
    /**
     * Amount of rows in the chest.
     */
    public static final int CHEST_INVENTORY_ROWS = 2;
    /**
     * Amount of columns in the chest.
     */
    public static final int CHEST_INVENTORY_COLUMNS = 5;

    public ContainerBloodChest(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new Inventory(TileBloodChest.SLOTS), Optional.empty());
    }

    public ContainerBloodChest(int id, PlayerInventory playerInventory, IInventory inventory,
                               Optional<TileBloodChest> tileSupplier) {
        super(RegistryEntries.CONTAINER_BLOOD_CHEST, id, playerInventory, inventory, tileSupplier, 0,
                CHEST_INVENTORY_ROWS, CHEST_INVENTORY_COLUMNS, CHEST_INVENTORY_OFFSET_X, CHEST_INVENTORY_OFFSET_Y);
        addSlot(new SlotFluidContainer(inventory, TileBloodChest.SLOT_CONTAINER, 28, 36, RegistryEntries.FLUID_BLOOD)); // Container emptier
        this.addPlayerInventory(playerInventory, INVENTORY_OFFSET_X, INVENTORY_OFFSET_Y);
        this.addPlayerArmorInventory(playerInventory, ARMOR_INVENTORY_OFFSET_X, ARMOR_INVENTORY_OFFSET_Y);
    }

    @Override
    public Slot makeSlot(IInventory inventory, int index, int row, int column) {
        return new SlotRepairable(inventory, index, row, column);
    }
    
}