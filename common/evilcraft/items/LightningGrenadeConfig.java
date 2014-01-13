package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ItemConfig;

public class LightningGrenadeConfig extends ItemConfig {
    
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
