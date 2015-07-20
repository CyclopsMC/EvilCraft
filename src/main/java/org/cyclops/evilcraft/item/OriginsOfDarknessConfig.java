package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Blood Orb.
 * @author rubensworks
 *
 */
public class OriginsOfDarknessConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static OriginsOfDarknessConfig _instance;

    /**
     * Make a new instance.
     */
    public OriginsOfDarknessConfig() {
        super(
                EvilCraft._instance,
        	true,
            "originsOfDarkness",
            null,
            OriginsOfDarkness.class
        );
    }
    
}
