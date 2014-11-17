package evilcraft.tileentity;

import evilcraft.core.tileentity.TankInventoryTileEntity;
import evilcraft.core.tileentity.WorkingTileEntity;
import evilcraft.core.tileentity.upgrade.Upgrades;
import evilcraft.item.Promise;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import java.util.Set;

/**
 * Evilcraft working tile entity with upgrade declaration.
 * @author rubensworks
 */
public abstract class TileWorking<T extends TankInventoryTileEntity> extends WorkingTileEntity<T> {

    public static final Item UPGRADE_ITEM = Promise.getInstance();

    private Set<Upgrades.Upgrade> upgradeTypes;
    private int currentTier = -1;

    /**
     * Make a new instance.
     * @param inventorySize Amount of slots in the inventory.
     * @param inventoryName Internal name of the inventory.
     * @param tankSize Size (mB) of the tank.
     * @param tankName Internal name of the tank.
     * @param acceptedFluid Type of Fluid to accept.
     * @param upgradeTypes The types of upgrade items.
     */
    public TileWorking(int inventorySize, String inventoryName,
                       int tankSize, String tankName, Fluid acceptedFluid,
                       Set<Upgrades.Upgrade> upgradeTypes) {
        super(inventorySize, inventoryName, tankSize, tankName, acceptedFluid);
        this.upgradeTypes = upgradeTypes;
    }

    public Upgrades.Upgrade getUpgradeType(ItemStack itemStack) {
        if(itemStack.getItem() == UPGRADE_ITEM) {
            Upgrades.Upgrade upgrade = Promise.getInstance().getUpgrade(itemStack);
            if(upgradeTypes.contains(upgrade)) {
                return upgrade;
            }
        }
        return null;
    }

    public int getUpgradeLevel(ItemStack itemStack) {
        return itemStack.stackSize;
    }

    protected void onUpgradeSlotChanged(int slotId, ItemStack oldItemStack, ItemStack itemStack) {
        resetTier();
        super.onUpgradeSlotChanged(slotId, oldItemStack, itemStack);
    }

    protected void resetTier() {
        this.currentTier = -1;
    }

    protected int getTier() {
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
    public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
        return super.canInsertItem(slot, itemStack, side) && canInsertItem(slot, itemStack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
        return super.canExtractItem(slot, itemStack, side) && canExtractItem(slot, itemStack, null);
    }

    /**
     * Check if the given item can be inserted.
     * @param slot The slot id.
     * @param itemStack The item that is being inserted.
     * @return If it can be inserted.
     */
    public boolean canInsertItem(int slot, ItemStack itemStack) {
        if(!isUpgradeSlot(slot)) return true;
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
