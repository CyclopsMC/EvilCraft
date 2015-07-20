package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

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
                EvilCraft._instance,
        	true,
            "bloodOrb",
            null,
            BloodOrb.class
        );
    }
    
}
