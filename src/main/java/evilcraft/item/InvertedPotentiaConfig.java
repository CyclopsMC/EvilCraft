package evilcraft.item;

import evilcraft.EvilCraft;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;

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
