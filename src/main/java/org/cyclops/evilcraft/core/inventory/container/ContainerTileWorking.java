package org.cyclops.evilcraft.core.inventory.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.inventory.slot.SlotExtended;
import org.cyclops.evilcraft.core.client.gui.container.ContainerScreenTileWorking;
import org.cyclops.evilcraft.core.tileentity.TileWorking;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Container for TileWorking instances.
 * @author rubensworks
 */
public abstract class ContainerTileWorking<T extends TileWorking<T, ?>> extends ContainerInventoryTickingTank<T> {

    public ContainerTileWorking(@Nullable ContainerType<?> type, int id, PlayerInventory playerInventory,
                                IInventory inventory, Optional<T> tileSupplier, int tickers, int upgradeSlots) {
        super(type, id, playerInventory, inventory, tileSupplier, tickers);
        this.offsetX = ContainerScreenTileWorking.UPGRADES_OFFSET_X;
    }

    public boolean isUpgradeSlotEnabled(int slot) {
        return getTileWorkingMetadata().isUpgradeSlotEnabled(this.inventory, getTileWorkingMetadata().getBasicInventorySize() + slot);
    }

    public abstract TileWorking.Metadata getTileWorkingMetadata();


    public void addUpgradeInventory(int offsetX, int offsetY, int slotStart) {
        int upgradeSlots = TileWorking.INVENTORY_SIZE_UPGRADES;
        int amount = 0;
        for(int i = slotStart; i < slotStart + upgradeSlots; i++) {
            addSlot(new SlotExtended(inventory, i, offsetX, offsetY + amount * ITEMBOX) {

                private ItemStack lastSlotContents = getStack();

                @Override
                public boolean isItemValid(ItemStack itemStack) {
                    return super.isItemValid(itemStack) && getTileWorkingMetadata().canInsertItem(inventory, getSlotIndex(), itemStack);
                }

                @Override
                public boolean canTakeStack(PlayerEntity playerIn) {
                    return super.canTakeStack(player) &&
                            getTileWorkingMetadata().canExtractItem(inventory, getSlotIndex(), getStack(), player.inventory.getItemStack());
                }

                @Override
                public void onSlotChanged() {
                    if(!ItemStack.areItemStacksEqual(lastSlotContents, this.getStack())) {
                        getTileSupplier().ifPresent(tile -> tile.onUpgradeSlotChanged(getSlotIndex(), lastSlotContents, this.getStack()));
                    }
                    lastSlotContents = this.getStack();
                    if(!lastSlotContents.isEmpty()) lastSlotContents = lastSlotContents.copy();
                }

            });
            amount++;
        }
    }

}
