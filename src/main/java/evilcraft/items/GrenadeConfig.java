package evilcraft.items;

import evilcraft.api.config.ItemConfig;

/**
 * Config for the {@link Grenade}.
 * @author immortaleeb
 */
public class GrenadeConfig extends ItemConfig {
    /**
     * The unique instance.
     */
    public static GrenadeConfig _instance;

    /**
     * Make a new instance.
     */
    public GrenadeConfig() {
        super(
            true,
            "grenade",
            null,
            Grenade.class
        );
    }
}
