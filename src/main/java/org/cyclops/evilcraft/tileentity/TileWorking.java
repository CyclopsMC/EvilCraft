package org.cyclops.evilcraft.tileentity;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import org.cyclops.cyclopscore.tileentity.TankInventoryTileEntity;
import org.cyclops.evilcraft.core.tileentity.WorkingTileEntity;
import org.cyclops.evilcraft.core.tileentity.upgrade.Upgrades;
import org.cyclops.evilcraft.item.Promise;

/**
 * Evilcraft working tile entity with upgrade declaration.
 * @param <O> The type of upgrade behaviour object.
 * @author rubensworks
 */
public abstract class TileWorking<T extends TankInventoryTileEntity, O> extends WorkingTileEntity<T, O> {

    public static final Item UPGRADE_ITEM = Promise.getInstance();

    private int currentTier = -1;

    /**
     * Make a new instance.
     * @param inventorySize Amount of slots in the inventory.
     * @param inventoryName Internal name of the inventory.
     * @param tankSize Size (mB) of the tank.
     * @param tankName Internal name of the tank.
     * @param acceptedFluid Type of Fluid to accept.
     */
    public TileWorking(int inventorySize, String inventoryName,
                       int tankSize, String tankName, Fluid acceptedFluid) {
        super(inventorySize, inventoryName, tankSize, tankName, acceptedFluid);
    }

    public Upgrades.Upgrade getUpgradeType(ItemStack itemStack) {
        if(itemStack.getItem() == UPGRADE_ITEM) {
            Upgrades.Upgrade upgrade = Promise.getInstance().getUpgrade(itemStack);
            if(getUpgrades().contains(upgrade)) {
                return upgrade;
            }
        }
        return null;
    }

    public int getUpgradeLevel(ItemStack itemStack) {
        return itemStack.stackSize;
    }

    public boolean onUpgradeSlotChanged(int slotId, ItemStack oldItemStack, ItemStack itemStack) {
        if(super.onUpgradeSlotChanged(slotId, oldItemStack, itemStack)) {
            resetTier();
            if(!worldObj.isRemote) getTank().setCapacity(getTankTierMultiplier(getTier()) * tankSize);
            sendUpdate();
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
                ItemStack itemStack = getStackInSlot(i);
                if(Promise.getInstance().isTierUpgrade(itemStack)) {
                    currentTier = Promise.getInstance().getUpgrade(itemStack).getTier();
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
            if(getStackInSlot(i) != null && !Promise.getInstance().isTierUpgrade(getStackInSlot(i))) return false;
        }
        return true;
    }

    public boolean isUpgradeSlotEnabled(int slotId) {
        return getRequiredTierForSlot(slotId) <= getTier();
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, EnumFacing side) {
        return super.canInsertItem(slot, itemStack, side) && canInsertItem(slot, itemStack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemStack, EnumFacing side) {
        return super.canExtractItem(slot, itemStack, side) && canExtractItem(slot, itemStack, (ItemStack) null);
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        return (isUpgradeSlot(slot) && isUpgradeSlotEnabled(slot)) || super.isItemValidForSlot(slot, itemStack);
    }

    /**
     * Check if the given item can be inserted.
     * @param slot The slot id.
     * @param itemStack The item that is being inserted.
     * @return If it can be inserted.
     */
    public boolean canInsertItem(int slot, ItemStack itemStack) {
        if(!isUpgradeSlot(slot)) return true;
        if(!getUpgrades().contains(Promise.getInstance().getUpgrade(itemStack))) return false;
        if(Promise.getInstance().isTierUpgrade(itemStack) && Promise.getInstance().isTierUpgrade(getStackInSlot(slot))) {
            // Condition already checked in canExtractItem.
            return true;
        } else if(Promise.getInstance().isTierUpgrade(getStackInSlot(slot))) {
            // Remove a tier upgrade
            return areUpgradeSlotsValidForTier(0);
        } else if(Promise.getInstance().isTierUpgrade(itemStack)) {
            // Add a tier upgrade
            return getTier() == 0 &&
                    getRequiredTierForSlot(slot) <= Promise.getInstance().getUpgrade(itemStack).getTier();
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
        if(!isUpgradeSlot(slot)) return true;
        if(Promise.getInstance().isTierUpgrade(itemStack) && Promise.getInstance().isTierUpgrade(replaceItem)) {
            // Swap tier upgrades
            int newTier = Promise.getInstance().getUpgrade(replaceItem).getTier();
            int currentTier = Promise.getInstance().getUpgrade(itemStack).getTier();
            return newTier >= currentTier || areUpgradeSlotsValidForTier(newTier);
        } else if(Promise.getInstance().isTierUpgrade(itemStack)) {
            // Remove the tier upgrade
            return areUpgradeSlotsValidForTier(0);
        }
        return true;
    }

}
