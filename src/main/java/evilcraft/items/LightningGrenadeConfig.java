package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ItemConfig;

/**
 * Config for the {@link LightningGrenade}.
 * @author rubensworks
 *
 */
public class LightningGrenadeConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static LightningGrenadeConfig _instance;

    /**
     * Make a new instance.
     */
    public LightningGrenadeConfig() {
        super(
        	true,
            "lightningGrenade",
            null,
            LightningGrenade.class
        );
    }
    
}
