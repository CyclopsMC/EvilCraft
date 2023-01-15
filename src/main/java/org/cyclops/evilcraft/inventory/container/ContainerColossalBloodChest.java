package org.cyclops.evilcraft.inventory.container;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.cyclops.cyclopscore.inventory.slot.SlotFluidContainer;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockColossalBloodChest;
import org.cyclops.evilcraft.blockentity.BlockEntityColossalBloodChest;
import org.cyclops.evilcraft.core.blockentity.BlockEntityWorking;
import org.cyclops.evilcraft.core.inventory.container.ContainerTileWorking;
import org.cyclops.evilcraft.inventory.slot.SlotRepairable;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Container for the {@link BlockColossalBloodChest}.
 * @author rubensworks
 *
 */
public class ContainerColossalBloodChest extends ContainerTileWorking<BlockEntityColossalBloodChest> {

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

    public ContainerColossalBloodChest(int id, Inventory playerInventory) {
        this(id, playerInventory, new SimpleContainer(BlockEntityColossalBloodChest.SLOTS + BlockEntityColossalBloodChest.INVENTORY_SIZE_UPGRADES), Optional.empty());
    }

    public ContainerColossalBloodChest(int id, Inventory playerInventory,
                                       Container inventory, Optional<BlockEntityColossalBloodChest> tileSupplier) {
        super(RegistryEntries.CONTAINER_COLOSSAL_BLOOD_CHEST, id, playerInventory, inventory, tileSupplier,
                BlockEntityColossalBloodChest.TICKERS, BlockEntityColossalBloodChest.INVENTORY_SIZE_UPGRADES);
        // Adding inventory
        addSlot(new SlotFluidContainer(inventory, BlockEntityColossalBloodChest.SLOT_CONTAINER,
                SLOT_CONTAINER_X, SLOT_CONTAINER_Y,
                RegistryEntries.FLUID_BLOOD)); // Container emptier

        addChestSlots(CHEST_INVENTORY_ROWS, CHEST_INVENTORY_COLUMNS);

        this.addUpgradeInventory(UPGRADE_INVENTORY_OFFSET_X, UPGRADE_INVENTORY_OFFSET_Y, BlockEntityColossalBloodChest.SLOTS);

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

    protected Slot makeSlot(Container inventory, int index, int row, int column) {
        return new SlotRepairable(inventory, index, row, column);
    }

    @Override
    public BlockEntityWorking.Metadata getTileWorkingMetadata() {
        return BlockEntityColossalBloodChest.METADATA;
    }
}
