package org.cyclops.evilcraft.inventory.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.cyclops.cyclopscore.helper.EntityHelpers;
import org.cyclops.cyclopscore.inventory.slot.SlotFluidContainer;
import org.cyclops.cyclopscore.inventory.slot.SlotRemoveOnly;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.api.gameevent.BloodInfuserRemoveEvent;
import org.cyclops.evilcraft.block.BlockBloodInfuser;
import org.cyclops.evilcraft.core.inventory.container.ContainerTileWorking;
import org.cyclops.evilcraft.core.inventory.slot.SlotWorking;
import org.cyclops.evilcraft.core.recipe.type.RecipeBloodInfuser;
import org.cyclops.evilcraft.core.tileentity.TileWorking;
import org.cyclops.evilcraft.tileentity.TileBloodInfuser;

import java.util.Optional;

/**
 * Container for the {@link BlockBloodInfuser}.
 * @author rubensworks
 *
 */
public class ContainerBloodInfuser extends ContainerTileWorking<TileBloodInfuser> {
    
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

    public ContainerBloodInfuser(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new Inventory(TileBloodInfuser.SLOTS + TileBloodInfuser.INVENTORY_SIZE_UPGRADES), Optional.empty());
    }

    public ContainerBloodInfuser(int id, PlayerInventory playerInventory, IInventory inventory,
                                 Optional<TileBloodInfuser> tileSupplier) {
        super(RegistryEntries.CONTAINER_BLOOD_INFUSER, id, playerInventory, inventory, tileSupplier,
                TileBloodInfuser.TICKERS, TileBloodInfuser.INVENTORY_SIZE_UPGRADES);

        // Adding inventory
        addSlot(new SlotFluidContainer(inventory, TileBloodInfuser.SLOT_CONTAINER, SLOT_CONTAINER_X, SLOT_CONTAINER_Y, RegistryEntries.FLUID_BLOOD)); // Container emptier
        addSlot(new SlotWorking<>(TileBloodInfuser.SLOT_INFUSE, SLOT_INFUSE_X, SLOT_INFUSE_Y, this, playerInventory.player.world)); // Infuse slot
        addSlot(new SlotRemoveOnly(inventory, TileBloodInfuser.SLOT_INFUSE_RESULT, SLOT_INFUSE_RESULT_X, SLOT_INFUSE_RESULT_Y) {
            @Override
            public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
                tileSupplier.ifPresent(tile -> {
                    EntityHelpers.spawnXpAtPlayer(player.world, player, (int) Math.floor(tile.getXp()));
                    tile.resetXp();
                    MinecraftForge.EVENT_BUS.post(new BloodInfuserRemoveEvent(player, stack));
                });
                return super.onTake(thePlayer, stack);
            }
        }); // Infuse result slot

        this.addUpgradeInventory(UPGRADE_INVENTORY_OFFSET_X, UPGRADE_INVENTORY_OFFSET_Y, TileBloodInfuser.SLOTS);

        this.addPlayerInventory(playerInventory, INVENTORY_OFFSET_X, INVENTORY_OFFSET_Y);
    }

    @Override
    public TileWorking.Metadata getTileWorkingMetadata() {
        return TileBloodInfuser.METADATA;
    }
}