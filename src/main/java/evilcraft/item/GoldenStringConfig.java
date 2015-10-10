package evilcraft.item;

import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Config for the Golden String.
 * @author rubensworks
 *
 */
public class GoldenStringConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static GoldenStringConfig _instance;

    /**
     * Make a new instance.
     */
    public GoldenStringConfig() {
        super(
            true,
            "goldenString",
            null,
            null
        );
    }
    
}
