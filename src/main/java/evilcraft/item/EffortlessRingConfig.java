package evilcraft.item;

import evilcraft.core.config.extendedconfig.ItemConfig;

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
        	true,
            "effortlessRing",
            null,
            EffortlessRing.class
        );
    }
    
}
