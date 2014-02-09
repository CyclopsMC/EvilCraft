package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ItemConfig;

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
            Reference.ITEM_BLOODINFUSIONCORE,
            "Blood Infusion Core",
            "bloodInfusionCore",
            null,
            BloodInfusionCore.class
        );
    }
    
}
