package evilcraft.item;

import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Config for the {@link evilcraft.item.Promise}.
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
        	true,
            "promise",
            null,
            Promise.class
        );
    }
    
}
