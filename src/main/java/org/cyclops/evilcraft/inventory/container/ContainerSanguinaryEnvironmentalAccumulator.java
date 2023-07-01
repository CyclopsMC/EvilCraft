package org.cyclops.evilcraft.inventory.container;

import net.minecraft.core.Vec3i;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import org.cyclops.cyclopscore.inventory.slot.SlotRemoveOnly;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockSanguinaryEnvironmentalAccumulator;
import org.cyclops.evilcraft.blockentity.BlockEntitySanguinaryEnvironmentalAccumulator;
import org.cyclops.evilcraft.core.blockentity.BlockEntityWorking;
import org.cyclops.evilcraft.core.inventory.container.ContainerTileWorking;
import org.cyclops.evilcraft.core.inventory.slot.SlotWorking;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Container for the {@link BlockSanguinaryEnvironmentalAccumulator}.
 * @author rubensworks
 *
 */
public class ContainerSanguinaryEnvironmentalAccumulator extends ContainerTileWorking<BlockEntitySanguinaryEnvironmentalAccumulator> {

    private static final int INVENTORY_OFFSET_X = 8;
    private static final int INVENTORY_OFFSET_Y = 84;

    /**
     * Accumulate slot X coordinate.
     */
    public static final int SLOT_ACCUMULATE_X = 54;
    /**
     * Accumulate slot Y coordinate.
     */
    public static final int SLOT_ACCUMULATE_Y = 36;

    /**
     * Accumulate result slot X coordinate.
     */
    public static final int SLOT_ACCUMULATE_RESULT_X = 108;
    /**
     * Accumulate result slot Y coordinate.
     */
    public static final int SLOT_ACCUMULATE_RESULT_Y = 36;

    private static final int UPGRADE_INVENTORY_OFFSET_X = -22;
    private static final int UPGRADE_INVENTORY_OFFSET_Y = 6;

    private final Supplier<Boolean> variableCanWork;
    private final Supplier<List<Vec3i>> variableInvalidLocations;

    public ContainerSanguinaryEnvironmentalAccumulator(int id, Inventory playerInventory) {
        this(id, playerInventory, new SimpleContainer(BlockEntitySanguinaryEnvironmentalAccumulator.SLOTS + BlockEntitySanguinaryEnvironmentalAccumulator.INVENTORY_SIZE_UPGRADES), Optional.empty());
    }

    public ContainerSanguinaryEnvironmentalAccumulator(int id, Inventory playerInventory, Container inventory,
                                                       Optional<BlockEntitySanguinaryEnvironmentalAccumulator> tileSupplier) {
        super(RegistryEntries.CONTAINER_SANGUINARY_ENVIRONMENTAL_ACCUMULATOR, id, playerInventory, inventory, tileSupplier,
                BlockEntitySanguinaryEnvironmentalAccumulator.TICKERS, BlockEntitySanguinaryEnvironmentalAccumulator.INVENTORY_SIZE_UPGRADES);

        this.variableCanWork = registerSyncedVariable(Boolean.class, () -> getTileSupplier().get().canWork());
        this.variableInvalidLocations = (Supplier) registerSyncedVariable(List.class, () -> getTileSupplier().get().getInvalidLocations());

        // Adding inventory
        addSlot(new SlotWorking<>(BlockEntitySanguinaryEnvironmentalAccumulator.SLOT_ACCUMULATE, SLOT_ACCUMULATE_X, SLOT_ACCUMULATE_Y, this, playerInventory.player.level())); // Accumulate slot
        addSlot(new SlotRemoveOnly(inventory, BlockEntitySanguinaryEnvironmentalAccumulator.SLOT_ACCUMULATE_RESULT, SLOT_ACCUMULATE_RESULT_X, SLOT_ACCUMULATE_RESULT_Y)); // Accumulate result slot

        this.addUpgradeInventory(UPGRADE_INVENTORY_OFFSET_X, UPGRADE_INVENTORY_OFFSET_Y, BlockEntitySanguinaryEnvironmentalAccumulator.SLOTS);

        this.addPlayerInventory(playerInventory, INVENTORY_OFFSET_X, INVENTORY_OFFSET_Y);
    }

    public boolean getTileCanWork() {
        return variableCanWork.get();
    }

    public List<Vec3i> getInvalidLocations() {
        return variableInvalidLocations.get();
    }

    @Override
    public BlockEntityWorking.Metadata getTileWorkingMetadata() {
        return BlockEntitySanguinaryEnvironmentalAccumulator.METADATA;
    }
}
