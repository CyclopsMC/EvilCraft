package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link Promise}.
 * @author rubensworks
 *
 */
public class PromiseConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static PromiseConfig _instance;

    /**
     * Make a new instance.
     */
    public PromiseConfig() {
        super(
                EvilCraft._instance,
        	true,
            "promise",
            null,
            Promise.class
        );
    }
}
