package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ItemConfig;

/**
 * Config for the {@link Blook}.
 * @author rubensworks
 *
 */
public class BlookConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static BlookConfig _instance;

    /**
     * Make a new instance.
     */
    public BlookConfig() {
        super(
            Reference.ITEM_BLOOK,
            "Blook",
            "blook",
            null,
            Blook.class
        );
    }
    
}
