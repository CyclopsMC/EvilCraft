package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ItemConfig;

/**
 * Config for the {@link Kineticator}.
 * @author rubensworks
 *
 */
public class KineticatorConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static KineticatorConfig _instance;

    /**
     * Make a new instance.
     */
    public KineticatorConfig() {
        super(
            Reference.ITEM_KINETICATOR,
            "Kineticator",
            "kineticator",
            null,
            Kineticator.class
        );
    }
    
}
