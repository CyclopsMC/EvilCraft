package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link BloodPotash}.
 * @author rubensworks
 *
 */
public class BloodPotashConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static BloodPotashConfig _instance;

    /**
     * Make a new instance.
     */
    public BloodPotashConfig() {
        super(
                EvilCraft._instance,
        	true,
            "bloodPotash",
            null,
            BloodPotash.class
        );
    }
    
}
