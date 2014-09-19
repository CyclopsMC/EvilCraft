package evilcraft.item;

import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Config for the {@link ExaltedCrafter}.
 * @author rubensworks
 *
 */
public class ExaltedCrafterConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static ExaltedCrafterConfig _instance;

    /**
     * Make a new instance.
     */
    public ExaltedCrafterConfig() {
        super(
        	true,
            "exaltedCrafter",
            null,
            ExaltedCrafter.class
        );
    }
    
}
