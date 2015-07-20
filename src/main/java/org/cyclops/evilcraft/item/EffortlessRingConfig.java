package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Effortless Ring.
 * @author rubensworks
 *
 */
public class EffortlessRingConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static EffortlessRingConfig _instance;

    /**
     * Make a new instance.
     */
    public EffortlessRingConfig() {
        super(
                EvilCraft._instance,
        	true,
            "effortlessRing",
            null,
            EffortlessRing.class
        );
    }
    
}
