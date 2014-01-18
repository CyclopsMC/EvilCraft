package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ItemConfig;

public class BloodInfusionCoreConfig extends ItemConfig {
    
    public static BloodInfusionCoreConfig _instance;

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
