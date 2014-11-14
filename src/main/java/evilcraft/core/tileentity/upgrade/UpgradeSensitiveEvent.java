package evilcraft.core.tileentity.upgrade;

/**
 * Default event implementation.
 * @author rubensworks
 */
public class UpgradeSensitiveEvent<O> implements IUpgradeSensitiveEvent<O> {

    private O object;

    public UpgradeSensitiveEvent(O object) {
        this.object = object;
    }

    @Override
    public O getObject() {
        return this.object;
    }

}
