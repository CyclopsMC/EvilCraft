package org.cyclops.evilcraft.core.blockentity.upgrade;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Factory class for creating new upgrade types.
 * These instances act as enums.
 * @author rubensworks
 */
public class Upgrades {

    private static final Map<String, Upgrade> upgradeMap = Maps.newHashMap();

    public static final int TIERS = 3;
    public static final Upgrade UPGRADE_TIER1 = getUpgrade("tier", 1);
    public static final Upgrade UPGRADE_TIER2 = getUpgrade("tier", 2);
    public static final Upgrade UPGRADE_TIER3 = getUpgrade("tier", 3);
    public static final Upgrade UPGRADE_SPEED = getUpgrade("speed");
    public static final Upgrade UPGRADE_EFFICIENCY = getUpgrade("efficiency");

    public static Collection<Upgrade> getUpgrades() {
        return upgradeMap.values();
    }

    /**
     * Get or create the unique upgrade instance
     * @param upgradeId The unique id of the upgrade.
     * @param tier the upgrade tier.
     * @return The upgrade instance.
     */
    public static Upgrade getUpgrade(String upgradeId, int tier) {
        Upgrade upgrade = upgradeMap.get(upgradeId + tier);
        if(upgrade == null) {
            upgrade = new Upgrade(upgradeId, tier);
            upgradeMap.put(upgradeId + tier, upgrade);
        }
        return upgrade;
    }

    /**
     * Get or create the unique upgrade instance
     * @param upgradeId The unique id of the upgrade.
     * @return The upgrade instance.
     */
    public static Upgrade getUpgrade(String upgradeId) {
        return getUpgrade(upgradeId, 0);
    }

    /**
     * @return A new unique UpgradeEventType.
     */
    public static UpgradeEventType newUpgradeEventType() {
        return new UpgradeEventType();
    }

    /**
     * Send an upgrade sensitive event over the event bus for all upgrades the upgradable has.
     * @param upgradable The upgradable instance.
     * @param event The event.
     * @param <T> The type of upgradable.
     * @param <O> The type of event variable type.
     */
    public static <T extends IUpgradable<T, O>, O> void sendEvent(T upgradable, IUpgradeSensitiveEvent<O> event) {
        sendEvent(upgradable, event, upgradable.getUpgradeBehaviour().keySet());
    }

    /**
     * Send an upgrade sensitive event over the event bus.
     * @param upgradable The upgradable instance.
     * @param event The event.
     * @param upgrade The types of upgrade this event should be sent to.
     * @param <T> The type of upgradable.
     * @param <O> The type of event variable type.
     */
    public static <T extends IUpgradable<T, O>, O> void sendEvent(T upgradable, IUpgradeSensitiveEvent<O> event, Upgrade upgrade) {
        sendEvent(upgradable, event, Lists.newArrayList(upgrade));
    }

    /**
     * Send an upgrade sensitive event over the event bus.
     * @param upgradable The upgradable instance.
     * @param event The event.
     * @param upgrades The types of upgrades this event should be sent to.
     * @param <T> The type of upgradable.
     * @param <O> The type of event variable type.
     */
    public static <T extends IUpgradable<T, O>, O> void sendEvent(T upgradable, IUpgradeSensitiveEvent<O> event, Upgrade... upgrades) {
        sendEvent(upgradable, event, Lists.newArrayList(upgrades));
    }

    /**
     * Send an upgrade sensitive event over the event bus.
     * @param upgradable The upgradable instance.
     * @param event The event.
     * @param upgrades The types of upgrades this event should be sent to.
     * @param <T> The type of upgradable.
     * @param <O> The type of event variable type.
     */
    public static <T extends IUpgradable<T, O>, O> void sendEvent(T upgradable, IUpgradeSensitiveEvent<O> event, Collection<Upgrade> upgrades) {
        for(Upgrades.Upgrade upgrade : upgrades) {
            IUpgradeBehaviour<T, O> behaviour = upgradable.getUpgradeBehaviour().get(upgrade);
            int upgradeLevel = behaviour.getUpgradeLevel(upgradable, upgrade);
            if(upgradeLevel > 0) {
                behaviour.applyUpgrade(upgradable, upgrade, upgradeLevel, event);
            }
        }
    }

    public static class Upgrade {

        private String id;
        private int tier;
        private Set<BlockConfig> upgradableInfo;

        private Upgrade(String id, int tier) {
            this.id = id;
            this.tier = tier;
            this.upgradableInfo = Sets.newTreeSet();
        }

        public String getId() {
            return this.id;
        }

        public int getTier() {
            return this.tier;
        }

        public void addUpgradableInfo(BlockConfig upgradableInfo) {
            this.upgradableInfo.add(upgradableInfo);
        }

        public Set<BlockConfig> getUpgradables() {
            return this.upgradableInfo;
        }

    }

    public static class UpgradeEventType {

        private UpgradeEventType() {

        }

    }

}
