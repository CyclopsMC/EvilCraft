package org.cyclops.evilcraft.inventory.container;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.cyclops.cyclopscore.inventory.slot.SlotFluidContainer;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityBloodChest;
import org.cyclops.evilcraft.core.inventory.container.ContainerTickingChest;
import org.cyclops.evilcraft.inventory.slot.SlotRepairable;

import java.util.Optional;

/**
 * Container for the {@link org.cyclops.evilcraft.block.BlockBloodChest}.
 * @author rubensworks
 *
 */
public class ContainerBloodChest extends ContainerTickingChest<BlockEntityBloodChest> {

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

    public ContainerBloodChest(int id, Inventory playerInventory) {
        this(id, playerInventory, new SimpleContainer(BlockEntityBloodChest.SLOTS), Optional.empty());
    }

    public ContainerBloodChest(int id, Inventory playerInventory, Container inventory,
                               Optional<BlockEntityBloodChest> tileSupplier) {
        super(RegistryEntries.CONTAINER_BLOOD_CHEST, id, playerInventory, inventory, tileSupplier, 0,
                CHEST_INVENTORY_ROWS, CHEST_INVENTORY_COLUMNS, CHEST_INVENTORY_OFFSET_X, CHEST_INVENTORY_OFFSET_Y);
        addSlot(new SlotFluidContainer(inventory, BlockEntityBloodChest.SLOT_CONTAINER, 28, 36, RegistryEntries.FLUID_BLOOD)); // Container emptier
        this.addPlayerInventory(playerInventory, INVENTORY_OFFSET_X, INVENTORY_OFFSET_Y);
        this.addPlayerArmorInventory(playerInventory, ARMOR_INVENTORY_OFFSET_X, ARMOR_INVENTORY_OFFSET_Y);
    }

    @Override
    public Slot makeSlot(Container inventory, int index, int row, int column) {
        return new SlotRepairable(inventory, index, row, column);
    }

}
