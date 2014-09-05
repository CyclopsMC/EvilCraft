package evilcraft.item;

import evilcraft.core.config.ItemConfig;

/**
 * Config for the {@link BloodInfusionCore}.
 * @author rubensworks
 *
 */
public class BloodInfusionCoreConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static BloodInfusionCoreConfig _instance;

    /**
     * Make a new instance.
     */
    public BloodInfusionCoreConfig() {
        super(
        	true,
            "bloodInfusionCore",
            null,
            BloodInfusionCore.class
        );
    }
    
}
