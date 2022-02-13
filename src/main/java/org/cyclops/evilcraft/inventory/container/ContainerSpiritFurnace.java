package org.cyclops.evilcraft.inventory.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.math.vector.Vector3i;
import org.cyclops.cyclopscore.inventory.slot.SlotFluidContainer;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockSpiritFurnace;
import org.cyclops.evilcraft.core.inventory.container.ContainerTileWorking;
import org.cyclops.evilcraft.core.inventory.slot.SlotWorking;
import org.cyclops.evilcraft.core.inventory.slot.SlotWorkingRemoveOnly;
import org.cyclops.evilcraft.core.tileentity.TileWorking;
import org.cyclops.evilcraft.tileentity.TileSpiritFurnace;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Container for the {@link BlockSpiritFurnace}.
 * @author rubensworks
 *
 */
public class ContainerSpiritFurnace extends ContainerTileWorking<TileSpiritFurnace> {
    
    private static final int INVENTORY_OFFSET_X = 8;
    private static final int INVENTORY_OFFSET_Y = 84;
    
    /**
     * Container slot X coordinate.
     */
    public static final int SLOT_CONTAINER_X = 8;
    /**
     * Container slot Y coordinate.
     */
    public static final int SLOT_CONTAINER_Y = 36;
    
    /**
     * Box slot X coordinate.
     */
    public static final int SLOT_BOX_X = 79;
    /**
     * Box slot Y coordinate.
     */
    public static final int SLOT_BOX_Y = 36;
    
    private static final int SLOTS_X = 2;
    private static final int SLOTS_Y = 2;
    /**
     * The amount of drop slots.
     */
    public static final int SLOTS_DROP = SLOTS_X * SLOTS_Y;
    /**
     * Drop slot X coordinate.
     */
    public static final int SLOT_DROP_X = 134;
    /**
     * Drop slot Y coordinate.
     */
    public static final int SLOT_DROP_Y = 28;

    private static final int UPGRADE_INVENTORY_OFFSET_X = -22;
    private static final int UPGRADE_INVENTORY_OFFSET_Y = 6;

    private final Supplier<Boolean> variableHasEntity;
    private final Supplier<Boolean> variableSizeValidForEntity;
    private final Supplier<Boolean> variableForceHalt;
    private final Supplier<Boolean> variableCaughtError;
    private final Supplier<Vector3i> variableInnerSize;
    private final Supplier<Vector3i> variableEntitySize;

    public ContainerSpiritFurnace(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new Inventory(TileSpiritFurnace.SLOTS + TileSpiritFurnace.INVENTORY_SIZE_UPGRADES), Optional.empty());
    }

    public ContainerSpiritFurnace(int id, PlayerInventory playerInventory, IInventory inventory,
                                 Optional<TileSpiritFurnace> tileSupplier) {
        super(RegistryEntries.CONTAINER_SPIRIT_FURNACE, id, playerInventory, inventory, tileSupplier,
                TileSpiritFurnace.TICKERS, TileSpiritFurnace.INVENTORY_SIZE_UPGRADES);

        this.variableHasEntity = registerSyncedVariable(Boolean.class, () -> getTileSupplier().get().getEntity() != null);
        this.variableSizeValidForEntity = registerSyncedVariable(Boolean.class, () -> getTileSupplier().get().isSizeValidForEntity());
        this.variableForceHalt = registerSyncedVariable(Boolean.class, () -> getTileSupplier().get().isForceHalt());
        this.variableCaughtError = registerSyncedVariable(Boolean.class, () -> getTileSupplier().get().isCaughtError());
        this.variableInnerSize = registerSyncedVariable(Vector3i.class, () -> getTileSupplier().get().getInnerSize());
        this.variableEntitySize = registerSyncedVariable(Vector3i.class, () -> getTileSupplier().get().getEntitySize());

        // Adding inventory
        addSlot(new SlotFluidContainer(inventory, TileSpiritFurnace.SLOT_CONTAINER,
        		SLOT_CONTAINER_X, SLOT_CONTAINER_Y,
        		RegistryEntries.FLUID_BLOOD)); // Container emptier
        addSlot(new SlotWorking<>(TileSpiritFurnace.SLOT_BOX, SLOT_BOX_X, SLOT_BOX_Y, this, playerInventory.player.level)); // Box slot
        
        int i = 0;
        for (int y = 0; y < SLOTS_X; y++) {
            for (int x = 0; x < SLOTS_Y; x++) {
            	addSlot(new SlotWorkingRemoveOnly<>(
                        TileSpiritFurnace.SLOTS_DROP[i], SLOT_DROP_X + x * ITEMBOX,
                        SLOT_DROP_Y + y * ITEMBOX, this, false, playerInventory.player.level)); // Drop slot
            	i++;
            }
        }

        this.addUpgradeInventory(UPGRADE_INVENTORY_OFFSET_X, UPGRADE_INVENTORY_OFFSET_Y, TileSpiritFurnace.SLOTS);

        this.addPlayerInventory(playerInventory, INVENTORY_OFFSET_X, INVENTORY_OFFSET_Y);
    }

    public boolean hasEntity() {
        return variableHasEntity.get();
    }

    public boolean isSizeValidForEntity() {
        return variableSizeValidForEntity.get();
    }

    public boolean isForceHalt() {
        return variableForceHalt.get();
    }

    public boolean isCaughtError() {
        return variableCaughtError.get();
    }

    public Vector3i getInnerSize() {
        return variableInnerSize.get();
    }

    public Vector3i getEntitySize() {
        return variableEntitySize.get();
    }

    @Override
    public TileWorking.Metadata getTileWorkingMetadata() {
        return TileSpiritFurnace.METADATA;
    }
}