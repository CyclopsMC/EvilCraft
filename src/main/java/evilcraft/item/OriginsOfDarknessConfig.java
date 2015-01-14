package evilcraft.item;

import evilcraft.core.config.extendedconfig.ItemConfig;

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
        	true,
            "originsOfDarkness",
            null,
            OriginsOfDarkness.class
        );
    }
    
}
