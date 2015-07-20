package org.cyclops.evilcraft.core.tileentity.upgrade;

/**
 * Upgrade events that should be used for sending into the Upgrades event-bus.
 * @author rubensworks
 */
public interface IUpgradeSensitiveEvent<O> {

    /**
     * @return The mutable event result object.
     */
    public O getObject();

    /**
     * @return The type of this event.
     */
    public Upgrades.UpgradeEventType getType();

}
