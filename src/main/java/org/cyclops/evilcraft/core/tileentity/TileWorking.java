package org.cyclops.evilcraft.core.tileentity;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.evilcraft.core.tileentity.upgrade.Upgrades;
import org.cyclops.evilcraft.item.ItemPromise;

/**
 * Evilcraft working tile entity with upgrade declaration.
 * @param <O> The type of upgrade behaviour object.
 * @author rubensworks
 */
public abstract class TileWorking<T extends TileWorking<T, O>, O> extends WorkingTileEntity<T, O> {

    private int currentTier = -1;

    public TileWorking(TileEntityType<?> type, int inventorySize, int stackSize, int tankSize, Fluid acceptedFluid) {
        super(type, inventorySize, stackSize, tankSize, acceptedFluid);
    }

    public Upgrades.Upgrade getUpgradeType(ItemStack itemStack) {
        if(itemStack.getItem() instanceof ItemPromise) {
            Upgrades.Upgrade upgrade = ((ItemPromise) itemStack.getItem()).getUpgrade(itemStack);
            if(getUpgrades().contains(upgrade)) {
                return upgrade;
            }
        }
        return null;
    }

    public int getUpgradeLevel(ItemStack itemStack) {
        return itemStack.getCount();
    }

    public boolean onUpgradeSlotChanged(int slotId, ItemStack oldItemStack, ItemStack itemStack) {
        if(super.onUpgradeSlotChanged(slotId, oldItemStack, itemStack)) {
            resetTier();
            if (!world.isRemote()) {
                getTank().setCapacity(getTankTierMultiplier(getTier()) * getTank().getCapacity());
            }
            markDirty();
            return true;
        }
        return false;
    }

    /**
     * Get the tank capacity multiplier.
     * @param tier The tier to multiply for.
     * @return The multiplier.
     */
    public static int getTankTierMultiplier(int tier) {
        return 1 << (tier * 2);
    }

    protected void resetTier() {
        this.currentTier = -1;
    }

    /**
     * @return The current tier of this machine.
     */
    public int getTier() {
        if(currentTier == -1) {
            for(int i = getBasicInventorySize(); i < getBasicInventorySize() + INVENTORY_SIZE_UPGRADES; i++) {
                ItemStack itemStack = getInventory().getStackInSlot(i);
                if (itemStack.getItem() instanceof ItemPromise && ((ItemPromise) itemStack.getItem()).isTierUpgrade(itemStack)) {
                    currentTier = ((ItemPromise) itemStack.getItem()).getUpgrade(itemStack).getTier();
                }
            }
            if(currentTier == -1) {
                currentTier = 0;
            }
        }
        return currentTier;
    }

    protected int getRequiredTierForSlot(int slotId) {
        if(!isUpgradeSlot(slotId)) {
            return 0;
        }
        return slotId - getBasicInventorySize();
    }

    protected boolean areUpgradeSlotsValidForTier(int tier) {
        for(int i = getBasicInventorySize() + tier + 1; i < getBasicInventorySize() + INVENTORY_SIZE_UPGRADES; i++) {
            ItemStack itemStack = getInventory().getStackInSlot(i);
            if (!(itemStack.getItem() instanceof ItemPromise && ((ItemPromise) itemStack.getItem()).isTierUpgrade(itemStack))) {
                return false;
            }
        }
        return true;
    }

    public boolean isUpgradeSlotEnabled(int slotId) {
        return getRequiredTierForSlot(slotId) <= getTier();
    }

    @Override
    protected SimpleInventory createInventory(int inventorySize, int stackSize) {
        return new Inventory<T>(inventorySize, stackSize, (T) this) {
            @Override
            public boolean canInsertItem(int slot, ItemStack itemStack, Direction side) {
                return super.canInsertItem(slot, itemStack, side) && canInsertItem(slot, itemStack, side);
            }

            @Override
            public boolean canExtractItem(int slot, ItemStack itemStack, Direction side) {
                return super.canExtractItem(slot, itemStack, side) && canExtractItem(slot, itemStack, side);
            }
        };
    }

    protected boolean isTierUpgrade(ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemPromise && ((ItemPromise) itemStack.getItem()).isTierUpgrade(itemStack);
    }

    /**
     * Check if the given item can be inserted.
     * @param slot The slot id.
     * @param itemStack The item that is being inserted.
     * @return If it can be inserted.
     */
    public boolean canInsertItem(int slot, ItemStack itemStack) {
        if (!isUpgradeSlot(slot)) return true;
        if (!(getUpgrades().contains(((ItemPromise) itemStack.getItem()).getUpgrade(itemStack)))) return false;
        if (isTierUpgrade(itemStack) && isTierUpgrade(getInventory().getStackInSlot(slot))) {
            // Condition already checked in canExtractItem.
            return true;
        } else if (isTierUpgrade(getInventory().getStackInSlot(slot))) {
            // Remove a tier upgrade
            return areUpgradeSlotsValidForTier(0);
        } else if (isTierUpgrade(itemStack)) {
            // Add a tier upgrade
            return getTier() == 0 &&
                    getRequiredTierForSlot(slot) <= ((ItemPromise) itemStack.getItem()).getUpgrade(itemStack).getTier();
        } else {
            // Non-tier upgrade
            return isUpgradeSlotEnabled(slot);
        }
    }

    /**
     * If the given item can be extracted.
     * @param slot The slot id.
     * @param itemStack The item that is being extracted, the current slot contents.
     * @param replaceItem The current item in that slot.
     * @return If it can be extracted.
     */
    public boolean canExtractItem(int slot, ItemStack itemStack, ItemStack replaceItem) {
        if (!isUpgradeSlot(slot)) return true;
        if (isTierUpgrade(itemStack) && isTierUpgrade(replaceItem)) {
            // Swap tier upgrades
            int newTier = ((ItemPromise) replaceItem.getItem()).getUpgrade(replaceItem).getTier();
            int currentTier = ((ItemPromise) itemStack.getItem()).getUpgrade(itemStack).getTier();
            return newTier >= currentTier || areUpgradeSlotsValidForTier(newTier);
        } else if (isTierUpgrade(itemStack)) {
            // Remove the tier upgrade
            return areUpgradeSlotsValidForTier(0);
        }
        return true;
    }

    public static class Inventory<T extends TileWorking<T, ?>> extends WorkingTileEntity.Inventory<T> {
        public Inventory(int size, int stackLimit, T tile) {
            super(size, stackLimit, tile);
        }

        @Override
        public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
            return (this.tile.isUpgradeSlot(slot) && this.tile.isUpgradeSlotEnabled(slot)) || super.isItemValidForSlot(slot, itemStack);
        }
    }

}
