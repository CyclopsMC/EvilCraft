package evilcraft.api.config;

/**
 * A helper class to contain callbacks for when the config file has been read
 * so that set values can be updated in the config objects.
 * @author rubensworks
 *
 */
public abstract class ConfigPropertyCallback {
    /**
     * Called when a config is updated.
     * @param newValue The new value of this config property.
     */
    public abstract void run(Object newValue);
}
