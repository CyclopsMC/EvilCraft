package evilcraft.core.tileentity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import evilcraft.core.tileentity.upgrade.IUpgradable;
import evilcraft.core.tileentity.upgrade.IUpgradeBehaviour;
import evilcraft.core.tileentity.upgrade.IUpgradeSensitiveEvent;
import evilcraft.core.tileentity.upgrade.Upgrades;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.List;
import java.util.Map;

/**
 * A TileEntity with that processes items with inventory and tank.
 * @author rubensworks
 * @param <T> The subclass of {@link TankInventoryTileEntity}, will be in
 * most cases just the extension class.
 * @see TickingTankInventoryTileEntity
 */
public abstract class WorkingTileEntity<T extends TankInventoryTileEntity> extends TickingTankInventoryTileEntity<T>
        implements IUpgradable {

    /**
     * Size of the upgrades inventory.
     */
    public static final int INVENTORY_SIZE_UPGRADES = 4;

    public static final Upgrades.Upgrade UPGRADE_TIER1 = Upgrades.getUpgrade("tier1");
    public static final Upgrades.Upgrade UPGRADE_TIER2 = Upgrades.getUpgrade("tier2");
    public static final Upgrades.Upgrade UPGRADE_TIER3 = Upgrades.getUpgrade("tier3");
    public static final Upgrades.Upgrade UPGRADE_SPEED = Upgrades.getUpgrade("speed");
    public static final Upgrades.Upgrade UPGRADE_EFFICIENCY = Upgrades.getUpgrade("efficiency");

    private int basicInventorySize;
    private Map<Upgrades.Upgrade, Integer> levels = null;

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
     * If the furnace should visually (block icon) show it is working, should only be
     * called client-side.
     * @return If the state is working.
     */
    public boolean isVisuallyWorking() {
        return getCurrentState() == 1;
    }
    
    /**
     * Get the work progress scaled, to be used in GUI's.
     * @param scale The scale this progress should be applied to.
     * @return The scaled working progress.
     */
    public int getWorkTickScaled(int scale) {
    	return (int) ((float)getWorkTick() / (float)getRequiredWorkTicks() * (float)scale);
    }
    
    protected abstract int getWorkTicker();
    
    protected int getWorkTick() {
        return getTickers().get(getWorkTicker()).getTick();
    }
    
    protected int getRequiredWorkTicks() {
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

    @Override
    protected void onInventoryChanged() {
        super.onInventoryChanged();
        resetUpgradeLevels();
    }

    @Override
    public void setInventorySlotContents(int slotId, ItemStack itemstack) {
        super.setInventorySlotContents(slotId, itemstack);
        if(slotId >= basicInventorySize && slotId < basicInventorySize + INVENTORY_SIZE_UPGRADES) {
            resetWork();
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
    public Map<Upgrades.Upgrade, IUpgradeBehaviour> getUpgrades() {
        Map<Upgrades.Upgrade, IUpgradeBehaviour> upgrades = Maps.newHashMap();
        upgrades.put(UPGRADE_SPEED, new IUpgradeBehaviour<WorkingTileEntity, MutableInt>() {

            @Override
            public int getUpgradeLevel(WorkingTileEntity upgradable, Upgrades.Upgrade upgrade) {
                Integer level = (Integer) upgradable.getUpgradeLevels().get(upgrade);
                return (level == null) ? 0 : level;
            }

            @Override
            public void applyUpgrade(WorkingTileEntity upgradable, Upgrades.Upgrade upgrade, int upgradeLevel,
                                     IUpgradeSensitiveEvent<MutableInt> event) {
                int duration = event.getObject().getValue();
                duration /= (1 + upgradeLevel / (3.2));
                event.getObject().setValue(duration);
            }

        });
        return upgrades;
    }

    /**
     * @return The inventory size without the upgrade slots taken into account.
     */
    public int getBasicInventorySize() {
        return this.basicInventorySize;
    }

}
