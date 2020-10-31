package org.cyclops.evilcraft.core.tileentity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.evilcraft.core.config.extendedconfig.UpgradableBlockContainerConfig;
import org.cyclops.evilcraft.core.tileentity.upgrade.IUpgradable;
import org.cyclops.evilcraft.core.tileentity.upgrade.IUpgradeBehaviour;
import org.cyclops.evilcraft.core.tileentity.upgrade.Upgrades;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A TileEntity with that processes items with inventory and tank.
 * @author rubensworks
 * @param <T> The subclass of {@link TankInventoryTileEntity}, will be in
 * most cases just the extension class.
 * @param <O> The type of upgrade behaviour object.
 * @see TickingTankInventoryTileEntity
 */
public abstract class WorkingTileEntity<T extends WorkingTileEntity<T, O>, O> extends TickingTankInventoryTileEntity<T>
        implements IUpgradable<T, O> {

    /**
     * Size of the upgrades inventory.
     */
    public static final int INVENTORY_SIZE_UPGRADES = 4;

    public static final int TIERS = 3;
    public static final Upgrades.Upgrade UPGRADE_TIER1 = Upgrades.getUpgrade("tier", 1);
    public static final Upgrades.Upgrade UPGRADE_TIER2 = Upgrades.getUpgrade("tier", 2);
    public static final Upgrades.Upgrade UPGRADE_TIER3 = Upgrades.getUpgrade("tier", 3);
    public static final Upgrades.Upgrade UPGRADE_SPEED = Upgrades.getUpgrade("speed");
    public static final Upgrades.Upgrade UPGRADE_EFFICIENCY = Upgrades.getUpgrade("efficiency");

    private int basicInventorySize;
    private Map<Upgrades.Upgrade, Integer> levels = null;
    protected Map<Upgrades.Upgrade, IUpgradeBehaviour<T, O>> upgradeBehaviour = Maps.newHashMap();

	public WorkingTileEntity(TileEntityType<?> type, int inventorySize, int stackSize, int tankSize, Fluid acceptedFluid) {
		super(type, inventorySize + INVENTORY_SIZE_UPGRADES, stackSize, tankSize, acceptedFluid);
        this.basicInventorySize = inventorySize;
	}

	public abstract IMetadata getTileWorkingMetadata();
    
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
    public void onStateChanged() {
        markDirty();
    }

    protected List<ItemStack> getUpgradeItems() {
        List<ItemStack> itemStacks = Lists.newLinkedList();
        for(int i = getBasicInventorySize(); i < getBasicInventorySize() + INVENTORY_SIZE_UPGRADES; i++) {
            ItemStack itemStack = getInventory().getStackInSlot(i);
            if(!itemStack.isEmpty()) {
                itemStacks.add(itemStack);
            }
        }
        return itemStacks;
    }

    /**
     * Get the type of upgrade corresponding to the given itemstack.
     * @param itemStack The itemstack. Not null.
     * @return The upgrade type.
     */
    public abstract Upgrades.Upgrade getUpgradeType(ItemStack itemStack);

    /**
     * Get the level of upgrade corresponding to the given itemstack.
     * @param itemStack The itemstack. Not null.
     * @return The upgrade level.
     */
    public abstract int getUpgradeLevel(ItemStack itemStack);

    protected void resetUpgradeLevels() {
        this.levels = null;
    }

    protected boolean isUpgradeSlot(int slotId) {
        return slotId >= basicInventorySize && slotId < basicInventorySize + INVENTORY_SIZE_UPGRADES;
    }

    public boolean onUpgradeSlotChanged(int slotId, ItemStack oldItemStack, ItemStack itemStack) {
        if(!ItemStack.areItemStacksEqual(oldItemStack, itemStack)) {
            resetUpgradeLevels();
            resetWork();
            return true;
        }
        return false;
    }

    @Override
    protected SimpleInventory createInventory(int inventorySize, int stackSize) {
        return new Inventory<>(inventorySize, stackSize, this);
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

    public Set<Upgrades.Upgrade> getUpgrades() {
        return UpgradableBlockContainerConfig.getBlockUpgrades(getBlockState().getBlock());
    }

    /**
     * @return The inventory size without the upgrade slots taken into account.
     */
    public int getBasicInventorySize() {
        return this.basicInventorySize;
    }

    public static class Inventory<T extends WorkingTileEntity<?, ?>> extends SimpleInventory {
        protected final T tile;

        public Inventory(int size, int stackLimit, T tile) {
            super(size, stackLimit);
            this.tile = tile;
        }

        @Override
        public ItemStack decrStackSize(int slotId, int count) {
            ItemStack itemStack = super.decrStackSize(slotId, count);
            if(this.tile.isUpgradeSlot(slotId)) {
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
            if(this.tile.isUpgradeSlot(slotId)) {
                this.tile.onUpgradeSlotChanged(slotId, oldItemStack, itemStack);
            }
        }
    }

    public static interface IMetadata {
        /**
         * Check if the given item can be infused.
         * @param itemStack The item to check.
         * @param world
         * @return If it can be infused.
         */
        public abstract boolean canConsume(ItemStack itemStack, World world);
    }

}
