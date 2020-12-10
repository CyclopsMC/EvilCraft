package org.cyclops.evilcraft.core.tileentity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.evilcraft.core.config.extendedconfig.UpgradableBlockContainerConfig;
import org.cyclops.evilcraft.core.tileentity.upgrade.IUpgradable;
import org.cyclops.evilcraft.core.tileentity.upgrade.IUpgradeBehaviour;
import org.cyclops.evilcraft.core.tileentity.upgrade.Upgrades;
import org.cyclops.evilcraft.item.ItemPromise;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Evilcraft working tile entity with upgrade declaration.
 * @param <O> The type of upgrade behaviour object.
 * @author rubensworks
 */
public abstract class TileWorking<T extends TileWorking<T, O>, O> extends TickingTankInventoryTileEntity<T>
        implements IUpgradable<T, O> {

    /**
     * Size of the upgrades inventory.
     */
    public static final int INVENTORY_SIZE_UPGRADES = 4;

    private int basicInventorySize;
    private Map<Upgrades.Upgrade, Integer> levels = null;
    protected Map<Upgrades.Upgrade, IUpgradeBehaviour<T, O>> upgradeBehaviour = Maps.newHashMap();

    private boolean hasJustWorked;

    public TileWorking(TileEntityType<?> type, int inventorySize, int stackSize, int tankSize, Fluid acceptedFluid) {
        super(type, inventorySize + INVENTORY_SIZE_UPGRADES, stackSize, tankSize, acceptedFluid);
        this.basicInventorySize = inventorySize;
    }

    public int getBasicInventorySize() {
        return basicInventorySize;
    }

    public abstract Metadata getTileWorkingMetadata();

    /**
     * Check if this tile is valid and can start working.
     * Mostly defined by environmental parameters.
     * @return If it is valid and can work.
     */
    public abstract boolean canWork();

    /**
     * If this tile is working.
     * @return If it is abstract.
     */
    public boolean isWorking() {
        return getWorkTick() > 0;
    }

    /**
     * If the furnace should visually (blockState icon) show it is working, should only be
     * called client-side.
     * @return If the state is working.
     */
    public boolean isVisuallyWorking() {
        return getCurrentState() == 1 && canWork();
    }

    /**
     * Get the work progress scaled, to be used in GUI's.
     * @param scale The scale this progress should be applied to.
     * @return The scaled working progress.
     */
    public int getWorkTickScaled(int scale) {
        return (int) Math.ceil((float)(getWorkTick() + 1) / (float)getRequiredWorkTicks() * (float)scale);
    }

    protected abstract int getWorkTicker();

    protected int getWorkTick() {
        return getTickers().get(getWorkTicker()).getTick();
    }

    protected float getRequiredWorkTicks() {
        return getTickers().get(getWorkTicker()).getRequiredTicks();
    }

    /**
     * Resets the ticks of the work.
     */
    public void resetWork() {
        resetWork(true);
    }

    /**
     * Resets the ticks of the work.
     * @param hardReset If the tick and required tick should also be set to zero.
     */
    public void resetWork(boolean hardReset) {
        if(hardReset) {
            getTickers().get(getWorkTicker()).setTick(0);
            getTickers().get(getWorkTicker()).setRequiredTicks(0);
        }
    }

    @Override
    public int getNewState() {
        return this.isWorking()?1:0;
    }

    @Override
    protected boolean hasJustWorked() {
        return this.hasJustWorked;
    }

    @Override
    public void updateTileEntity() {
        this.hasJustWorked = isWorking();
        super.updateTileEntity();
    }

    @Override
    public void onStateChanged() {
        markDirty();
    }

    protected List<ItemStack> getUpgradeItems() {
        List<ItemStack> itemStacks = Lists.newLinkedList();
        for(int i = this.basicInventorySize; i < this.basicInventorySize + INVENTORY_SIZE_UPGRADES; i++) {
            ItemStack itemStack = getInventory().getStackInSlot(i);
            if(!itemStack.isEmpty()) {
                itemStacks.add(itemStack);
            }
        }
        return itemStacks;
    }

    protected void resetUpgradeLevels() {
        this.levels = null;
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

    public Map<Upgrades.Upgrade, Integer> getUpgradeLevels() {
        if(levels == null) {
            levels = Maps.newHashMap();
            for (ItemStack itemStack : getUpgradeItems()) {
                Upgrades.Upgrade upgrade = getUpgradeType(itemStack);
                int level = getUpgradeLevel(itemStack);
                if (levels.containsKey(upgrade)) {
                    level += levels.get(upgrade);
                }
                levels.put(upgrade, level);
            }
        }
        return levels;
    }

    @Override
    public Map<Upgrades.Upgrade, IUpgradeBehaviour<T, O>> getUpgradeBehaviour() {
        return upgradeBehaviour;
    }

    /**
     * Get the type of upgrade corresponding to the given itemstack.
     * @param itemStack The itemstack. Not null.
     * @return The upgrade type.
     */
    public Upgrades.Upgrade getUpgradeType(ItemStack itemStack) {
        if(itemStack.getItem() instanceof ItemPromise) {
            Upgrades.Upgrade upgrade = ((ItemPromise) itemStack.getItem()).getUpgrade(itemStack);
            if(getTileWorkingMetadata().getUpgrades().contains(upgrade)) {
                return upgrade;
            }
        }
        return null;
    }

    /**
     * Get the level of upgrade corresponding to the given itemstack.
     * @param itemStack The itemstack. Not null.
     * @return The upgrade level.
     */
    public int getUpgradeLevel(ItemStack itemStack) {
        return itemStack.getCount();
    }

    public boolean onUpgradeSlotChanged(int slotId, ItemStack oldItemStack, ItemStack itemStack) {
        if(!ItemStack.areItemStacksEqual(oldItemStack, itemStack)) {
            resetUpgradeLevels();
            resetWork();
            if (!world.isRemote()) {
                getTank().setCapacity(getTankTierMultiplier(getTileWorkingMetadata().getTier(getInventory())) * tankSize);
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

    public static class Inventory<T extends TileWorking<T, ?>> extends SimpleInventory {
        protected final T tile;

        public Inventory(int size, int stackLimit, T tile) {
            super(size, stackLimit);
            this.tile = tile;
        }

        @Override
        public ItemStack decrStackSize(int slotId, int count) {
            ItemStack itemStack = super.decrStackSize(slotId, count);
            if(this.tile.getTileWorkingMetadata().isUpgradeSlot(slotId)) {
                ItemStack oldItemStack = itemStack.copy();
                oldItemStack.grow(count);
                this.tile.onUpgradeSlotChanged(slotId, oldItemStack, itemStack);
            }
            return itemStack;
        }

        @Override
        public void setInventorySlotContents(int slotId, ItemStack itemStack) {
            ItemStack oldItemStack = getStackInSlot(slotId);
            if(!oldItemStack.isEmpty()) oldItemStack = oldItemStack.copy();
            super.setInventorySlotContents(slotId, itemStack);
            if(this.tile.getTileWorkingMetadata().isUpgradeSlot(slotId)) {
                this.tile.onUpgradeSlotChanged(slotId, oldItemStack, itemStack);
            }
        }

        @Override
        public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
            return (this.tile.getTileWorkingMetadata().isUpgradeSlot(slot)
                    && this.tile.getTileWorkingMetadata().isUpgradeSlotEnabled(this.tile.getInventory(), slot))
                    || super.isItemValidForSlot(slot, itemStack);
        }
    }

    public static abstract class Metadata {

        private final int basicInventorySize;

        protected Metadata(int basicInventorySize) {
            this.basicInventorySize = basicInventorySize;
        }

        public int getBasicInventorySize() {
            return basicInventorySize;
        }

        /**
         * Check if the given item can be infused.
         * @param itemStack The item to check.
         * @param world The world.
         * @return If it can be infused.
         */
        public abstract boolean canConsume(ItemStack itemStack, World world);

        /**
         * Check if the given item can be inserted.
         * @param inventory The tile inventory.
         * @param slot The slot id.
         * @param itemStack The item that is being inserted.
         * @return If it can be inserted.
         */
        public boolean canInsertItem(IInventory inventory, int slot, ItemStack itemStack) {
            if (!this.isUpgradeSlot(slot)) return true;
            if (!(itemStack.getItem() instanceof ItemPromise)
                    || !(this.getUpgrades().contains(((ItemPromise) itemStack.getItem()).getUpgrade(itemStack)))) return false;
            if (this.isTierUpgrade(itemStack) && this.isTierUpgrade(inventory.getStackInSlot(slot))) {
                // Condition already checked in canExtractItem.ø
                return true;
            } else if (this.isTierUpgrade(inventory.getStackInSlot(slot))) {
                // Remove a tier upgrade
                return this.areUpgradeSlotsValidForTier(inventory, 0);
            } else if (this.isTierUpgrade(itemStack)) {
                // Add a tier upgrade
                return this.getTier(inventory) == 0 &&
                        this.getRequiredTierForSlot(slot) <= ((ItemPromise) itemStack.getItem()).getUpgrade(itemStack).getTier();
            } else {
                // Non-tier upgrade
                return this.isUpgradeSlotEnabled(inventory, slot);
            }
        }

        /**
         * If the given item can be extracted.
         * @param inventory The tile inventory.
         * @param slot The slot id.
         * @param itemStack The item that is being extracted, the current slot contents.
         * @param replaceItem The current item in that slot.
         * @return If it can be extracted.
         */
        public boolean canExtractItem(IInventory inventory, int slot, ItemStack itemStack, ItemStack replaceItem) {
            if (!isUpgradeSlot(slot)) return true;
            if (isTierUpgrade(itemStack) && isTierUpgrade(replaceItem)) {
                // Swap tier upgrades
                int newTier = ((ItemPromise) replaceItem.getItem()).getUpgrade(replaceItem).getTier();
                int currentTier = ((ItemPromise) itemStack.getItem()).getUpgrade(itemStack).getTier();
                return newTier >= currentTier || areUpgradeSlotsValidForTier(inventory, newTier);
            } else if (isTierUpgrade(itemStack)) {
                // Remove the tier upgrade
                return areUpgradeSlotsValidForTier(inventory, 0);
            }
            return true;
        }

        protected boolean isUpgradeSlot(int slotId) {
            return slotId >= basicInventorySize && slotId < basicInventorySize + INVENTORY_SIZE_UPGRADES;
        }

        protected boolean isTierUpgrade(ItemStack itemStack) {
            return itemStack.getItem() instanceof ItemPromise && ((ItemPromise) itemStack.getItem()).isTierUpgrade(itemStack);
        }

        protected boolean areUpgradeSlotsValidForTier(IInventory inventory, int tier) {
            for(int i = basicInventorySize + tier + 1; i < basicInventorySize + INVENTORY_SIZE_UPGRADES; i++) {
                ItemStack itemStack = inventory.getStackInSlot(i);
                if (!itemStack.isEmpty() && !(itemStack.getItem() instanceof ItemPromise && ((ItemPromise) itemStack.getItem()).isTierUpgrade(itemStack))) {
                    return false;
                }
            }
            return true;
        }

        public boolean isUpgradeSlotEnabled(IInventory inventory, int slotId) {
            return getRequiredTierForSlot(slotId) <= getTier(inventory);
        }

        protected int getRequiredTierForSlot(int slotId) {
            if(!isUpgradeSlot(slotId)) {
                return 0;
            }
            return slotId - basicInventorySize;
        }

        /**
         * @param inventory The tile inventory.
         * @return The current tier of this machine.
         */
        public int getTier(IInventory inventory) {
            for(int i = basicInventorySize; i < basicInventorySize + INVENTORY_SIZE_UPGRADES; i++) {
                ItemStack itemStack = inventory.getStackInSlot(i);
                if (itemStack.getItem() instanceof ItemPromise && ((ItemPromise) itemStack.getItem()).isTierUpgrade(itemStack)) {
                    return ((ItemPromise) itemStack.getItem()).getUpgrade(itemStack).getTier();
                }
            }
            return 0;
        }

        public Set<Upgrades.Upgrade> getUpgrades() {
            return UpgradableBlockContainerConfig.getBlockUpgrades(getBlock());
        }

        protected abstract Block getBlock();
    }

}
