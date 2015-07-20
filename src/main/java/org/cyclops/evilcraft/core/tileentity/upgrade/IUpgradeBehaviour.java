package org.cyclops.evilcraft.core.tileentity.upgrade;

/**
 * Behaviour for an upgrade in an upgradable instance.
 * @author rubensworks
 */
public interface IUpgradeBehaviour<T, O> {

    /**
     * @param upgradable The upgradable on which the behaviour was defined.
     * @param upgrade The upgrade.
     * @return The level of upgrade, no level is 0.
     */
    public int getUpgradeLevel(T upgradable, Upgrades.Upgrade upgrade);

    /**
     * Callback for when an event occurs that is sensitive to upgrades.
     * @param upgradable The upgradable on which the behaviour was defined.
     * @param upgrade The upgrade to apply to the event, checks for application to this instance is already done.
     * @param upgradeLevel The level of upgrade.
     * @param event The event.
     */
    public void applyUpgrade(T upgradable, Upgrades.Upgrade upgrade, int upgradeLevel, IUpgradeSensitiveEvent<O> event);

}
