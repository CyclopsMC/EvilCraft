package org.cyclops.evilcraft.inventory.container;

import net.minecraft.core.Vec3i;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import org.cyclops.cyclopscore.inventory.slot.SlotFluidContainer;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockSpiritFurnace;
import org.cyclops.evilcraft.blockentity.BlockEntitySpiritFurnace;
import org.cyclops.evilcraft.core.blockentity.BlockEntityWorking;
import org.cyclops.evilcraft.core.inventory.container.ContainerTileWorking;
import org.cyclops.evilcraft.core.inventory.slot.SlotWorking;
import org.cyclops.evilcraft.core.inventory.slot.SlotWorkingRemoveOnly;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Container for the {@link BlockSpiritFurnace}.
 * @author rubensworks
 *
 */
public class ContainerSpiritFurnace extends ContainerTileWorking<BlockEntitySpiritFurnace> {

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
    private final Supplier<Vec3i> variableInnerSize;
    private final Supplier<Vec3i> variableEntitySize;

    public ContainerSpiritFurnace(int id, Inventory playerInventory) {
        this(id, playerInventory, new SimpleContainer(BlockEntitySpiritFurnace.SLOTS + BlockEntitySpiritFurnace.INVENTORY_SIZE_UPGRADES), Optional.empty());
    }

    public ContainerSpiritFurnace(int id, Inventory playerInventory, Container inventory,
                                 Optional<BlockEntitySpiritFurnace> tileSupplier) {
        super(RegistryEntries.CONTAINER_SPIRIT_FURNACE, id, playerInventory, inventory, tileSupplier,
                BlockEntitySpiritFurnace.TICKERS, BlockEntitySpiritFurnace.INVENTORY_SIZE_UPGRADES);

        this.variableHasEntity = registerSyncedVariable(Boolean.class, () -> getTileSupplier().get().getEntity() != null);
        this.variableSizeValidForEntity = registerSyncedVariable(Boolean.class, () -> getTileSupplier().get().isSizeValidForEntity());
        this.variableForceHalt = registerSyncedVariable(Boolean.class, () -> getTileSupplier().get().isForceHalt());
        this.variableCaughtError = registerSyncedVariable(Boolean.class, () -> getTileSupplier().get().isCaughtError());
        this.variableInnerSize = registerSyncedVariable(Vec3i.class, () -> getTileSupplier().get().getInnerSize());
        this.variableEntitySize = registerSyncedVariable(Vec3i.class, () -> getTileSupplier().get().getEntitySize());

        // Adding inventory
        addSlot(new SlotFluidContainer(inventory, BlockEntitySpiritFurnace.SLOT_CONTAINER,
                SLOT_CONTAINER_X, SLOT_CONTAINER_Y,
                RegistryEntries.FLUID_BLOOD)); // Container emptier
        addSlot(new SlotWorking<>(BlockEntitySpiritFurnace.SLOT_BOX, SLOT_BOX_X, SLOT_BOX_Y, this, playerInventory.player.level())); // Box slot

        int i = 0;
        for (int y = 0; y < SLOTS_X; y++) {
            for (int x = 0; x < SLOTS_Y; x++) {
                addSlot(new SlotWorkingRemoveOnly<>(
                        BlockEntitySpiritFurnace.SLOTS_DROP[i], SLOT_DROP_X + x * ITEMBOX,
                        SLOT_DROP_Y + y * ITEMBOX, this, false, playerInventory.player.level())); // Drop slot
                i++;
            }
        }

        this.addUpgradeInventory(UPGRADE_INVENTORY_OFFSET_X, UPGRADE_INVENTORY_OFFSET_Y, BlockEntitySpiritFurnace.SLOTS);

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

    public Vec3i getInnerSize() {
        return variableInnerSize.get();
    }

    public Vec3i getEntitySize() {
        return variableEntitySize.get();
    }

    @Override
    public BlockEntityWorking.Metadata getTileWorkingMetadata() {
        return BlockEntitySpiritFurnace.METADATA;
    }
}
