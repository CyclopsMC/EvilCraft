package evilcraft.item;

import evilcraft.core.config.ItemConfig;

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
            true,
            "invertedPotentia",
            null,
            InvertedPotentia.class
        );
    }
    
}
