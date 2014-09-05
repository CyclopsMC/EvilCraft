package evilcraft.item;

import evilcraft.core.config.ItemConfig;

/**
 * Config for the {@link Blook}.
 * @author rubensworks
 *
 */
public class BlookConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static BlookConfig _instance;

    /**
     * Make a new instance.
     */
    public BlookConfig() {
        super(
            true,
            "blook",
            null,
            Blook.class
        );
    }
    
}
