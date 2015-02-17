package evilcraft.modcompat.thaumcraft;

import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Config for the {@link evilcraft.modcompat.thaumcraft.BloodWandCap}.
 * @author rubensworks
 *
 */
public class BloodWandCapConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static BloodWandCapConfig _instance;

    /**
     * Make a new instance.
     */
    public BloodWandCapConfig() {
        super(
        	true,
            "bloodWandCap",
            null,
            BloodWandCap.class
        );
    }
    
}
