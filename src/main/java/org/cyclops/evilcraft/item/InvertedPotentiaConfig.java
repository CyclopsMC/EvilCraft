package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link InvertedPotentia}.
 * @author rubensworks
 *
 */
public class InvertedPotentiaConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static InvertedPotentiaConfig _instance;

    /**
     * Make a new instance.
     */
    public InvertedPotentiaConfig() {
        super(
                EvilCraft._instance,
            true,
            "invertedPotentia",
            null,
            InvertedPotentia.class
        );
    }
    
}
