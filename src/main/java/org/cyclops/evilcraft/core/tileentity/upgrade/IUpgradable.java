package org.cyclops.evilcraft.core.tileentity.upgrade;

import java.util.Map;

/**
 * Interface for objects that are upgradable.
 * @author rubensworks
 */
public interface IUpgradable<T, O> {

    /**
     * @return The possible upgrades.
     */
    public Map<Upgrades.Upgrade, IUpgradeBehaviour<T, O>> getUpgradeBehaviour();

}
