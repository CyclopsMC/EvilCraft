package evilcraft.core.tileentity.upgrade;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Factory class for creating new upgrade types.
 * These instances act as enums.
 * @author rubensworks
 */
public class Upgrades {

    private static Map<String, Upgrade> upgradeMap = Maps.newHashMap();

    /**
     * Get or create the unique upgrade instance
     * @param upgradeId The unique id of the upgrade.
     * @return The upgrade instance.
     */
    public static Upgrade getUpgrade(String upgradeId) {
        Upgrade upgrade = upgradeMap.get(upgradeId);
        if(upgrade == null) {
            upgrade = new Upgrade();
            upgradeMap.put(upgradeId, upgrade);
        }
        return upgrade;
    }

    /**
     * Send an upgrade sensitive event over the event bus.
     * @param upgradable The upgradable instance.
     * @param event The event.
     * @param <T> The type of upgradable.
     * @param <O> The type of event variable type.
     */
    public static <T, O> void sendEvent(IUpgradable upgradable, IUpgradeSensitiveEvent<O> event) {
        for(Map.Entry<Upgrades.Upgrade, IUpgradeBehaviour> entry : upgradable.getUpgrades().entrySet()) {
            Upgrades.Upgrade upgrade = entry.getKey();
            IUpgradeBehaviour behaviour = entry.getValue();
            int upgradeLevel = behaviour.getUpgradeLevel(upgradable, upgrade);
            if(upgradeLevel > 0) {
                behaviour.applyUpgrade(upgradable, upgrade, upgradeLevel, event);
            }
        }
    }

    public static class Upgrade {

        private Upgrade() {

        }

    }

}
