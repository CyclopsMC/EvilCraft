package org.cyclops.evilcraft.core.inventory.container;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.inventory.slot.SlotExtended;
import org.cyclops.evilcraft.core.blockentity.BlockEntityWorking;
import org.cyclops.evilcraft.core.client.gui.container.ContainerScreenTileWorking;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Container for TileWorking instances.
 * @author rubensworks
 */
public abstract class ContainerTileWorking<T extends BlockEntityWorking<T, ?>> extends ContainerInventoryTickingTank<T> {

    public ContainerTileWorking(@Nullable MenuType<?> type, int id, Inventory playerInventory,
                                Container inventory, Optional<T> tileSupplier, int tickers, int upgradeSlots) {
        super(type, id, playerInventory, inventory, tileSupplier, tickers);
        this.offsetX = ContainerScreenTileWorking.UPGRADES_OFFSET_X;
    }

    public boolean isUpgradeSlotEnabled(int slot) {
        return getTileWorkingMetadata().isUpgradeSlotEnabled(this.inventory, getTileWorkingMetadata().getBasicInventorySize() + slot);
    }

    public abstract BlockEntityWorking.Metadata getTileWorkingMetadata();


    public void addUpgradeInventory(int offsetX, int offsetY, int slotStart) {
        int upgradeSlots = BlockEntityWorking.INVENTORY_SIZE_UPGRADES;
        int amount = 0;
        for(int i = slotStart; i < slotStart + upgradeSlots; i++) {
            addSlot(new SlotExtended(inventory, i, offsetX, offsetY + amount * ITEMBOX) {

                private ItemStack lastSlotContents = getItem();

                @Override
                public boolean mayPlace(ItemStack itemStack) {
                    return super.mayPlace(itemStack) && getTileWorkingMetadata().canInsertItem(container, getSlotIndex(), itemStack);
                }

                @Override
                public boolean mayPickup(Player playerIn) {
                    return super.mayPickup(player) &&
                            getTileWorkingMetadata().canExtractItem(container, getSlotIndex(), getItem(), getCarried());
                }

                @Override
                public void setChanged() {
                    if(!ItemStack.matches(lastSlotContents, this.getItem())) {
                        getTileSupplier().ifPresent(tile -> tile.onUpgradeSlotChanged(getSlotIndex(), lastSlotContents, this.getItem()));
                    }
                    lastSlotContents = this.getItem();
                    if(!lastSlotContents.isEmpty()) lastSlotContents = lastSlotContents.copy();
                }

            });
            amount++;
        }
    }

}
