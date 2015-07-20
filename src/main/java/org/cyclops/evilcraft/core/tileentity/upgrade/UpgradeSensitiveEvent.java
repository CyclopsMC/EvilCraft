package org.cyclops.evilcraft.core.tileentity.upgrade;

/**
 * Default event implementation.
 * @author rubensworks
 */
public class UpgradeSensitiveEvent<O> implements IUpgradeSensitiveEvent<O> {

    private O object;
    private Upgrades.UpgradeEventType type;

    public UpgradeSensitiveEvent(O object, Upgrades.UpgradeEventType type) {
        this.object = object;
        this.type = type;
    }

    @Override
    public O getObject() {
        return this.object;
    }

    @Override
    public Upgrades.UpgradeEventType getType() {
        return type;
    }

}
