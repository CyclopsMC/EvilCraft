package org.cyclops.evilcraft.inventory.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import org.cyclops.cyclopscore.inventory.slot.SlotFluidContainer;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockColossalBloodChest;
import org.cyclops.evilcraft.core.inventory.container.ContainerTileWorking;
import org.cyclops.evilcraft.core.tileentity.WorkingTileEntity;
import org.cyclops.evilcraft.inventory.slot.SlotRepairable;
import org.cyclops.evilcraft.tileentity.TileColossalBloodChest;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Container for the {@link BlockColossalBloodChest}.
 * @author rubensworks
 *
 */
public class ContainerColossalBloodChest extends ContainerTileWorking<TileColossalBloodChest> {

    private static final int INVENTORY_OFFSET_X = 28;
    private static final int INVENTORY_OFFSET_Y = 107;
    private static final int ARMOR_INVENTORY_OFFSET_X = 192;
    private static final int ARMOR_INVENTORY_OFFSET_Y = 109;

    private static final int CHEST_INVENTORY_OFFSET_X = 59;
    private static final int CHEST_INVENTORY_OFFSET_Y = 13;
    /**
     * Amount of rows in the chest.
     */
    public static final int CHEST_INVENTORY_ROWS = 5;
    /**
     * Amount of columns in the chest.
     */
    public static final int CHEST_INVENTORY_COLUMNS = 9;

    /**
     * Container slot X coordinate.
     */
    public static final int SLOT_CONTAINER_X = 6;
    /**
     * Container slot Y coordinate.
     */
    public static final int SLOT_CONTAINER_Y = 46;

    private static final int UPGRADE_INVENTORY_OFFSET_X = -22;
    private static final int UPGRADE_INVENTORY_OFFSET_Y = 6;

    private final Supplier<Integer> variableEfficiency;

    public ContainerColossalBloodChest(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new Inventory(TileColossalBloodChest.SLOTS + TileColossalBloodChest.INVENTORY_SIZE_UPGRADES), Optional.empty());
    }

    public ContainerColossalBloodChest(int id, PlayerInventory playerInventory,
                                       IInventory inventory, Optional<TileColossalBloodChest> tileSupplier) {
        super(RegistryEntries.CONTAINER_COLOSSAL_BLOOD_CHEST, id, playerInventory, inventory, tileSupplier,
                TileColossalBloodChest.TICKERS, TileColossalBloodChest.INVENTORY_SIZE_UPGRADES);
        // Adding inventory
        addSlot(new SlotFluidContainer(inventory, TileColossalBloodChest.SLOT_CONTAINER,
        		SLOT_CONTAINER_X, SLOT_CONTAINER_Y,
                RegistryEntries.FLUID_BLOOD)); // Container emptier
        
        addChestSlots(CHEST_INVENTORY_ROWS, CHEST_INVENTORY_COLUMNS);

        this.addUpgradeInventory(UPGRADE_INVENTORY_OFFSET_X, UPGRADE_INVENTORY_OFFSET_Y, TileColossalBloodChest.INVENTORY_SIZE_UPGRADES);

        this.addPlayerInventory(playerInventory, INVENTORY_OFFSET_X, INVENTORY_OFFSET_Y);
        this.addPlayerArmorInventory(playerInventory, ARMOR_INVENTORY_OFFSET_X, ARMOR_INVENTORY_OFFSET_Y);

        this.variableEfficiency = registerSyncedVariable(Integer.class, () -> getTileSupplier().get().getEfficiency());
    }

    public int getEfficiency() {
        return variableEfficiency.get();
    }

    protected void addChestSlots(int rows, int columns) {
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                addSlot(makeSlot(inventory, column + row * columns, CHEST_INVENTORY_OFFSET_X + column * 18, CHEST_INVENTORY_OFFSET_Y + row * 18));
            }
        }
    }

    protected Slot makeSlot(IInventory inventory, int index, int row, int column) {
        return new SlotRepairable(inventory, index, row, column);
    }

    @Override
    public WorkingTileEntity.IMetadata getTileWorkingMetadata() {
        return TileColossalBloodChest.METADATA;
    }
}