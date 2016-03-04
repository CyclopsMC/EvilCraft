package org.cyclops.evilcraft.core.tileentity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import org.cyclops.cyclopscore.tileentity.TankInventoryTileEntity;
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
public abstract class WorkingTileEntity<T extends TankInventoryTileEntity, O> extends TickingTankInventoryTileEntity<T>
        implements IUpgradable<T, O> {

    /**
     * Size of the upgrades inventory.
     */
    public static final int INVENTORY_SIZE_UPGRADES = 4;

    public static final Upgrades.Upgrade UPGRADE_TIER1 = Upgrades.getUpgrade("tier", 1);
    public static final Upgrades.Upgrade UPGRADE_TIER2 = Upgrades.getUpgrade("tier", 2);
    public static final Upgrades.Upgrade UPGRADE_TIER3 = Upgrades.getUpgrade("tier", 3);
    public static final Upgrades.Upgrade UPGRADE_SPEED = Upgrades.getUpgrade("speed");
    public static final Upgrades.Upgrade UPGRADE_EFFICIENCY = Upgrades.getUpgrade("efficiency");

    private int basicInventorySize;
    private Map<Upgrades.Upgrade, Integer> levels = null;
    protected Map<Upgrades.Upgrade, IUpgradeBehaviour<T, O>> upgradeBehaviour = Maps.newHashMap();

	/**
     * Make a new instance.
     * @param inventorySize Amount of slots in the inventory.
     * @param inventoryName Internal name of the inventory.
     * @param tankSize Size (mB) of the tank.
     * @param tankName Internal name of the tank.
     * @param acceptedFluid Type of Fluid to accept.
     */
	public WorkingTileEntity(int inventorySize, String inventoryName,
			int tankSize, String tankName, Fluid acceptedFluid) {
		super(inventorySize + INVENTORY_SIZE_UPGRADES, inventoryName, tankSize, tankName, acceptedFluid);
        this.basicInventorySize = inventorySize;
	}

	 /**
     * Check if the given item can be infused.
     * @param itemStack The item to check.
     * @return If it can be infused.
     */
    public abstract boolean canConsume(ItemStack itemStack);
    
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
        sendUpdate();
    }

    protected List<ItemStack> getUpgradeItems() {
        List<ItemStack> itemStacks = Lists.newLinkedList();
        for(int i = getBasicInventorySize(); i < getBasicInventorySize() + INVENTORY_SIZE_UPGRADES; i++) {
            ItemStack itemStack = getStackInSlot(i);
            if(itemStack != null) {
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
    public ItemStack decrStackSize(int slotId, int count) {
        ItemStack itemStack = super.decrStackSize(slotId, count);
        if(isUpgradeSlot(slotId)) {
            ItemStack oldItemStack = itemStack.copy();
            oldItemStack.stackSize += count;
            onUpgradeSlotChanged(slotId, oldItemStack, itemStack);
        }
        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int slotId, ItemStack itemStack) {
        ItemStack oldItemStack = getStackInSlot(slotId);
        if(oldItemStack != null) oldItemStack = oldItemStack.copy();
        super.setInventorySlotContents(slotId, itemStack);
        if(isUpgradeSlot(slotId)) {
            onUpgradeSlotChanged(slotId, oldItemStack, itemStack);
        }
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
        return ((UpgradableBlockContainerConfig) this.getBlock().getConfig()).getUpgrades();
    }

    /**
     * @return The inventory size without the upgrade slots taken into account.
     */
    public int getBasicInventorySize() {
        return this.basicInventorySize;
    }

}
