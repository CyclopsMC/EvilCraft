package evilcraft.item;

import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Config for the Blood Orb.
 * @author rubensworks
 *
 */
public class BloodOrbConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static BloodOrbConfig _instance;

    /**
     * Make a new instance.
     */
    public BloodOrbConfig() {
        super(
        	true,
            "bloodOrb",
            null,
            BloodOrb.class
        );
    }
    
}
