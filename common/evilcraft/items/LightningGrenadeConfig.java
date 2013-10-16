package evilcraft.items;

import evilcraft.api.config.ExtendedConfig;

public class LightningGrenadeConfig extends ExtendedConfig {
    
    public static LightningGrenadeConfig _instance;

    public LightningGrenadeConfig() {
        super(
            1,
            "Lightning Grenade",
            "lightninggrenade",
            null,
            LightningGrenade.class
        );
    }
    
}
