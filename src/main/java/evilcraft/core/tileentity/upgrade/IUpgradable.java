package evilcraft.core.tileentity.upgrade;

import java.util.Map;

/**
 * Interface for objects that are upgradable.
 * @author rubensworks
 */
public interface IUpgradable {

    /**
     * @return The possible upgrades.
     */
    public Map<Upgrades.Upgrade, IUpgradeBehaviour> getUpgrades();

}
