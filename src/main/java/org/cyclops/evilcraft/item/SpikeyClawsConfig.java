package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link SpikeyClaws}.
 * @author rubensworks
 *
 */
public class SpikeyClawsConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static SpikeyClawsConfig _instance;

    /**
     * Make a new instance.
     */
    public SpikeyClawsConfig() {
        super(
                EvilCraft._instance,
        	true,
            "spikey_claws",
            null,
            SpikeyClaws.class
        );
    }
    
}
