package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Blood Infusion Core.
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
                EvilCraft._instance,
        	true,
            "bloodInfusionCore",
            null,
            null
        );
    }
    
}
