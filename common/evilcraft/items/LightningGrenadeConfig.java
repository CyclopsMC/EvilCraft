package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig;

public class LightningGrenadeConfig extends ExtendedConfig {
    
    public static LightningGrenadeConfig _instance;

    public LightningGrenadeConfig() {
        super(
            Reference.ITEM_LIGHTNINGGRENADE,
            "Lightning Pearl",
            "lightningGrenade",
            null,
            LightningGrenade.class
        );
    }
    
}
