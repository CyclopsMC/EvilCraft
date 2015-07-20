package org.cyclops.evilcraft.block;

import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link LightningBomb}.
 * @author rubensworks
 *
 */
public class LightningBombConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static LightningBombConfig _instance;

    /**
     * Make a new instance.
     */
    public LightningBombConfig() {
        super(
                EvilCraft._instance,
        	true,
            "lightningBomb",
            null,
            LightningBomb.class
        );
    }
    
}
