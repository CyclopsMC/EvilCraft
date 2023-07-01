package org.cyclops.evilcraft.inventory.container;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.cyclopscore.inventory.slot.SlotFluidContainer;
import org.cyclops.cyclopscore.inventory.slot.SlotRemoveOnly;
import org.cyclops.cyclopscore.inventory.slot.SlotSingleItem;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockSpiritReanimator;
import org.cyclops.evilcraft.blockentity.BlockEntitySpiritReanimator;
import org.cyclops.evilcraft.core.blockentity.BlockEntityWorking;
import org.cyclops.evilcraft.core.inventory.container.ContainerTileWorking;
import org.cyclops.evilcraft.core.inventory.slot.SlotWorking;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Container for the {@link BlockSpiritReanimator}.
 * @author rubensworks
 *
 */
public class ContainerSpiritReanimator extends ContainerTileWorking<BlockEntitySpiritReanimator> {

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
    public static final int SLOT_BOX_X = 97;
    /**
     * Box slot Y coordinate.
     */
    public static final int SLOT_BOX_Y = 22;

    /**
     * Egg slot X coordinate.
     */
    public static final int SLOT_EGG_X = 135;
    /**
     * Egg slot Y coordinate.
     */
    public static final int SLOT_EGG_Y = 22;

    /**
     * Output slot X coordinate.
     */
    public static final int SLOT_OUTPUT_X = 116;
    /**
     * Output slot Y coordinate.
     */
    public static final int SLOT_OUTPUT_Y = 52;

    private static final int UPGRADE_INVENTORY_OFFSET_X = -22;
    private static final int UPGRADE_INVENTORY_OFFSET_Y = 6;

    private final Supplier<String> variableEntityName;

    public ContainerSpiritReanimator(int id, Inventory playerInventory) {
        this(id, playerInventory, new SimpleContainer(BlockEntitySpiritReanimator.SLOTS + BlockEntitySpiritReanimator.INVENTORY_SIZE_UPGRADES), Optional.empty());
    }

    public ContainerSpiritReanimator(int id, Inventory playerInventory, Container inventory,
                                  Optional<BlockEntitySpiritReanimator> tileSupplier) {
        super(RegistryEntries.CONTAINER_SPIRIT_REANIMATOR, id, playerInventory, inventory, tileSupplier,
                BlockEntitySpiritReanimator.TICKERS, BlockEntitySpiritReanimator.INVENTORY_SIZE_UPGRADES);

        this.variableEntityName = registerSyncedVariable(String.class, () -> getTileSupplier().get().getEntityType() == null ? null : ForgeRegistries.ENTITY_TYPES.getKey(getTileSupplier().get().getEntityType()).toString());

        // Adding inventory
        addSlot(new SlotFluidContainer(inventory, BlockEntitySpiritReanimator.SLOT_CONTAINER,
                SLOT_CONTAINER_X, SLOT_CONTAINER_Y,
                RegistryEntries.FLUID_BLOOD)); // Container emptier
        addSlot(new SlotWorking<BlockEntitySpiritReanimator>(BlockEntitySpiritReanimator.SLOT_BOX, SLOT_BOX_X, SLOT_BOX_Y, this, playerInventory.player.level())); // Box slot
        addSlot(new SlotSingleItem(inventory, BlockEntitySpiritReanimator.SLOT_EGG, SLOT_EGG_X, SLOT_EGG_Y, Items.EGG));
        addSlot(new SlotRemoveOnly(inventory, BlockEntitySpiritReanimator.SLOTS_OUTPUT, SLOT_OUTPUT_X, SLOT_OUTPUT_Y));

        this.addUpgradeInventory(UPGRADE_INVENTORY_OFFSET_X, UPGRADE_INVENTORY_OFFSET_Y, BlockEntitySpiritReanimator.SLOTS);

        this.addPlayerInventory(playerInventory, INVENTORY_OFFSET_X, INVENTORY_OFFSET_Y);
    }

    @Nullable
    public String getEntityName() {
        return variableEntityName.get();
    }

    @Override
    public BlockEntityWorking.Metadata getTileWorkingMetadata() {
        return BlockEntitySpiritReanimator.METADATA;
    }

}
