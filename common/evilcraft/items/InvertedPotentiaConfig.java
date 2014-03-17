package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ItemConfig;

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
            Reference.ITEM_INVERTEDPOTENTIA,
            "Inverted Potentia",
            "invertedPotentia",
            null,
            InvertedPotentia.class
        );
    }
    
}
