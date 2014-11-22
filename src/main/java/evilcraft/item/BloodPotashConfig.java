package evilcraft.item;

import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Config for the {@link evilcraft.item.BloodPotash}.
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
        	true,
            "bloodPotash",
            null,
            BloodPotash.class
        );
    }
    
}
