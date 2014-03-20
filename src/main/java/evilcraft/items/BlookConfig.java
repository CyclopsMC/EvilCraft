package evilcraft.items;

import evilcraft.api.config.ItemConfig;

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
