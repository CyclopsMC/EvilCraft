package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Blook.
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
                EvilCraft._instance,
            true,
            "blook",
            null,
            null
        );
    }
    
}
