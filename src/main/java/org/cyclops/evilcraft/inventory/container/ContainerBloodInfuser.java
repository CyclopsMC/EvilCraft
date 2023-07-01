package org.cyclops.evilcraft.inventory.container;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.cyclops.cyclopscore.helper.EntityHelpers;
import org.cyclops.cyclopscore.inventory.slot.SlotFluidContainer;
import org.cyclops.cyclopscore.inventory.slot.SlotRemoveOnly;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.api.gameevent.BloodInfuserRemoveEvent;
import org.cyclops.evilcraft.block.BlockBloodInfuser;
import org.cyclops.evilcraft.blockentity.BlockEntityBloodInfuser;
import org.cyclops.evilcraft.core.blockentity.BlockEntityWorking;
import org.cyclops.evilcraft.core.inventory.container.ContainerTileWorking;
import org.cyclops.evilcraft.core.inventory.slot.SlotWorking;

import java.util.Optional;

/**
 * Container for the {@link BlockBloodInfuser}.
 * @author rubensworks
 *
 */
public class ContainerBloodInfuser extends ContainerTileWorking<BlockEntityBloodInfuser> {

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
     * Infuse slot X coordinate.
     */
    public static final int SLOT_INFUSE_X = 79;
    /**
     * Infuse slot Y coordinate.
     */
    public static final int SLOT_INFUSE_Y = 36;

    /**
     * Infuse result slot X coordinate.
     */
    public static final int SLOT_INFUSE_RESULT_X = 133;
    /**
     * Infuse result slot Y coordinate.
     */
    public static final int SLOT_INFUSE_RESULT_Y = 36;

    private static final int UPGRADE_INVENTORY_OFFSET_X = -22;
    private static final int UPGRADE_INVENTORY_OFFSET_Y = 6;

    public ContainerBloodInfuser(int id, Inventory playerInventory) {
        this(id, playerInventory, new SimpleContainer(BlockEntityBloodInfuser.SLOTS + BlockEntityBloodInfuser.INVENTORY_SIZE_UPGRADES), Optional.empty());
    }

    public ContainerBloodInfuser(int id, Inventory playerInventory, Container inventory,
                                 Optional<BlockEntityBloodInfuser> tileSupplier) {
        super(RegistryEntries.CONTAINER_BLOOD_INFUSER, id, playerInventory, inventory, tileSupplier,
                BlockEntityBloodInfuser.TICKERS, BlockEntityBloodInfuser.INVENTORY_SIZE_UPGRADES);

        // Adding inventory
        addSlot(new SlotFluidContainer(inventory, BlockEntityBloodInfuser.SLOT_CONTAINER, SLOT_CONTAINER_X, SLOT_CONTAINER_Y, RegistryEntries.FLUID_BLOOD)); // Container emptier
        addSlot(new SlotWorking<>(BlockEntityBloodInfuser.SLOT_INFUSE, SLOT_INFUSE_X, SLOT_INFUSE_Y, this, playerInventory.player.level())); // Infuse slot
        addSlot(new SlotRemoveOnly(inventory, BlockEntityBloodInfuser.SLOT_INFUSE_RESULT, SLOT_INFUSE_RESULT_X, SLOT_INFUSE_RESULT_Y) {
            @Override
            public void onTake(Player thePlayer, ItemStack stack) {
                tileSupplier.ifPresent(tile -> {
                    EntityHelpers.spawnXpAtPlayer(player.level(), player, (int) Math.floor(tile.getXp()));
                    tile.resetXp();
                    MinecraftForge.EVENT_BUS.post(new BloodInfuserRemoveEvent(player, stack));
                });
                super.onTake(thePlayer, stack);
            }
        }); // Infuse result slot

        this.addUpgradeInventory(UPGRADE_INVENTORY_OFFSET_X, UPGRADE_INVENTORY_OFFSET_Y, BlockEntityBloodInfuser.SLOTS);

        this.addPlayerInventory(playerInventory, INVENTORY_OFFSET_X, INVENTORY_OFFSET_Y);
    }

    @Override
    public BlockEntityWorking.Metadata getTileWorkingMetadata() {
        return BlockEntityBloodInfuser.METADATA;
    }
}
