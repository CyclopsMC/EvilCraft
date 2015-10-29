package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

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
                EvilCraft._instance,
            true,
            "goldenString",
            null,
            null
        );
    }
    
}
