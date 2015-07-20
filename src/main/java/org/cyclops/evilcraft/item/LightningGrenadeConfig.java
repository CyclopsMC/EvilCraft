package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

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
                EvilCraft._instance,
        	true,
            "lightningGrenade",
            null,
            LightningGrenade.class
        );
    }
    
}
