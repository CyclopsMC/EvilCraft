package org.cyclops.evilcraft.core.tileentity.upgrade;

import org.cyclops.evilcraft.core.tileentity.WorkingTileEntity;

/**
 * Behaviour for upgrade events that have a simple integer value to change.
 * @author rubensworks
 */
public abstract class UpgradeBehaviour<T extends WorkingTileEntity, O> implements IUpgradeBehaviour<T, O> {

    protected double valueFactor;

    /**
     * Make a new instance.
     * @param valueFactor The factor to remove from the resulting value per upgrade level.
     *                    Formula used: value /= (1 + upgradeLevel / valueFactor);
     */
    public UpgradeBehaviour(double valueFactor) {
        this.valueFactor = valueFactor;
    }

    @Override
    public int getUpgradeLevel(T upgradable, Upgrades.Upgrade upgrade) {
        Integer level = (Integer) upgradable.getUpgradeLevels().get(upgrade);
        return (level == null) ? 0 : level;
    }

    @Override
    public abstract void applyUpgrade(T upgradable, Upgrades.Upgrade upgrade, int upgradeLevel,
                             IUpgradeSensitiveEvent<O> event);

}
