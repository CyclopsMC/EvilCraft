package org.cyclops.evilcraft.inventory.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.math.BlockPos;
import org.cyclops.cyclopscore.inventory.slot.SlotRemoveOnly;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockSanguinaryEnvironmentalAccumulator;
import org.cyclops.evilcraft.core.inventory.container.ContainerTileWorking;
import org.cyclops.evilcraft.core.inventory.slot.SlotWorking;
import org.cyclops.evilcraft.core.tileentity.TileWorking;
import org.cyclops.evilcraft.tileentity.TileSanguinaryEnvironmentalAccumulator;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Container for the {@link BlockSanguinaryEnvironmentalAccumulator}.
 * @author rubensworks
 *
 */
public class ContainerSanguinaryEnvironmentalAccumulator extends ContainerTileWorking<TileSanguinaryEnvironmentalAccumulator> {

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
    private final Supplier<List<BlockPos>> variableInvalidLocations;

    public ContainerSanguinaryEnvironmentalAccumulator(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new Inventory(TileSanguinaryEnvironmentalAccumulator.SLOTS + TileSanguinaryEnvironmentalAccumulator.INVENTORY_SIZE_UPGRADES), Optional.empty());
    }

    public ContainerSanguinaryEnvironmentalAccumulator(int id, PlayerInventory playerInventory, IInventory inventory,
                                                       Optional<TileSanguinaryEnvironmentalAccumulator> tileSupplier) {
        super(RegistryEntries.CONTAINER_SANGUINARY_ENVIRONMENTAL_ACCUMULATOR, id, playerInventory, inventory, tileSupplier,
                TileSanguinaryEnvironmentalAccumulator.TICKERS, TileSanguinaryEnvironmentalAccumulator.INVENTORY_SIZE_UPGRADES);

        this.variableCanWork = registerSyncedVariable(Boolean.class, () -> getTileSupplier().get().canWork());
        this.variableInvalidLocations = (Supplier) registerSyncedVariable(List.class, () -> getTileSupplier().get().getInvalidLocations());

        // Adding inventory
        addSlot(new SlotWorking<>(TileSanguinaryEnvironmentalAccumulator.SLOT_ACCUMULATE, SLOT_ACCUMULATE_X, SLOT_ACCUMULATE_Y, this, playerInventory.player.world)); // Accumulate slot
        addSlot(new SlotRemoveOnly(inventory, TileSanguinaryEnvironmentalAccumulator.SLOT_ACCUMULATE_RESULT, SLOT_ACCUMULATE_RESULT_X, SLOT_ACCUMULATE_RESULT_Y)); // Accumulate result slot

        this.addUpgradeInventory(UPGRADE_INVENTORY_OFFSET_X, UPGRADE_INVENTORY_OFFSET_Y, TileSanguinaryEnvironmentalAccumulator.SLOTS);

        this.addPlayerInventory(playerInventory, INVENTORY_OFFSET_X, INVENTORY_OFFSET_Y);
    }

    public boolean getTileCanWork() {
        return variableCanWork.get();
    }

    public List<BlockPos> getInvalidLocations() {
        return variableInvalidLocations.get();
    }

    @Override
    public TileWorking.Metadata getTileWorkingMetadata() {
        return TileSanguinaryEnvironmentalAccumulator.METADATA;
    }
}