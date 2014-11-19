package evilcraft.tileentity;

import evilcraft.core.tileentity.WorkingTileEntity;
import evilcraft.core.tileentity.upgrade.IUpgradeBehaviour;
import evilcraft.core.tileentity.upgrade.IUpgradeSensitiveEvent;
import evilcraft.core.tileentity.upgrade.Upgrades;
import org.apache.commons.lang3.mutable.MutableInt;

/**
 * Behaviour for upgrade events that have a simple integer value to change.
 * @author rubensworks
 */
public abstract class MutableIntUpgradeBehaviour implements IUpgradeBehaviour<WorkingTileEntity, MutableInt> {

    protected double valueFactor;

    /**
     * Make a new instance.
     * @param valueFactor The factor to remove from the resulting value per upgrade level.
     *                    Formula used: value /= (1 + upgradeLevel / valueFactor);
     */
    public MutableIntUpgradeBehaviour(double valueFactor) {
        this.valueFactor = valueFactor;
    }

    @Override
    public int getUpgradeLevel(WorkingTileEntity upgradable, Upgrades.Upgrade upgrade) {
        Integer level = (Integer) upgradable.getUpgradeLevels().get(upgrade);
        return (level == null) ? 0 : level;
    }

    @Override
    public abstract void applyUpgrade(WorkingTileEntity upgradable, Upgrades.Upgrade upgrade, int upgradeLevel,
                             IUpgradeSensitiveEvent<MutableInt> event);

}
