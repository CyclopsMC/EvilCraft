package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Corrupted Tear.
 * @author rubensworks
 *
 */
public class CorruptedTearConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static CorruptedTearConfig _instance;

    /**
     * Make a new instance.
     */
    public CorruptedTearConfig() {
        super(
                EvilCraft._instance,
            true,
            "corruptedTear",
            null,
            null
        );
    }
    
}
