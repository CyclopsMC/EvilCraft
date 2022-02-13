package org.cyclops.evilcraft.inventory.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Items;
import org.cyclops.cyclopscore.inventory.slot.SlotFluidContainer;
import org.cyclops.cyclopscore.inventory.slot.SlotRemoveOnly;
import org.cyclops.cyclopscore.inventory.slot.SlotSingleItem;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockSpiritReanimator;
import org.cyclops.evilcraft.core.inventory.container.ContainerTileWorking;
import org.cyclops.evilcraft.core.inventory.slot.SlotWorking;
import org.cyclops.evilcraft.core.tileentity.TileWorking;
import org.cyclops.evilcraft.tileentity.TileSpiritReanimator;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Container for the {@link BlockSpiritReanimator}.
 * @author rubensworks
 *
 */
public class ContainerSpiritReanimator extends ContainerTileWorking<TileSpiritReanimator> {
    
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

    public ContainerSpiritReanimator(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new Inventory(TileSpiritReanimator.SLOTS + TileSpiritReanimator.INVENTORY_SIZE_UPGRADES), Optional.empty());
    }

    public ContainerSpiritReanimator(int id, PlayerInventory playerInventory, IInventory inventory,
                                  Optional<TileSpiritReanimator> tileSupplier) {
        super(RegistryEntries.CONTAINER_SPIRIT_REANIMATOR, id, playerInventory, inventory, tileSupplier,
                TileSpiritReanimator.TICKERS, TileSpiritReanimator.INVENTORY_SIZE_UPGRADES);

        this.variableEntityName = registerSyncedVariable(String.class, () -> getTileSupplier().get().getEntityType() == null ? null : getTileSupplier().get().getEntityType().getRegistryName().toString());

        // Adding inventory
        addSlot(new SlotFluidContainer(inventory, TileSpiritReanimator.SLOT_CONTAINER,
        		SLOT_CONTAINER_X, SLOT_CONTAINER_Y,
        		RegistryEntries.FLUID_BLOOD)); // Container emptier
        addSlot(new SlotWorking<TileSpiritReanimator>(TileSpiritReanimator.SLOT_BOX, SLOT_BOX_X, SLOT_BOX_Y, this, playerInventory.player.level)); // Box slot
        addSlot(new SlotSingleItem(inventory, TileSpiritReanimator.SLOT_EGG, SLOT_EGG_X, SLOT_EGG_Y, Items.EGG));
        addSlot(new SlotRemoveOnly(inventory, TileSpiritReanimator.SLOTS_OUTPUT, SLOT_OUTPUT_X, SLOT_OUTPUT_Y));

        this.addUpgradeInventory(UPGRADE_INVENTORY_OFFSET_X, UPGRADE_INVENTORY_OFFSET_Y, TileSpiritReanimator.SLOTS);

        this.addPlayerInventory(playerInventory, INVENTORY_OFFSET_X, INVENTORY_OFFSET_Y);
    }

    @Nullable
    public String getEntityName() {
        return variableEntityName.get();
    }

    @Override
    public TileWorking.Metadata getTileWorkingMetadata() {
        return TileSpiritReanimator.METADATA;
    }
    
}