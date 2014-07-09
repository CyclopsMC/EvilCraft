package evilcraft.items;

import evilcraft.api.config.ItemConfig;

/**
 * Config for the {@link VengeanceFocus}.
 * @author rubensworks
 *
 */
public class VengeanceFocusConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static VengeanceFocusConfig _instance;

    /**
     * Make a new instance.
     */
    public VengeanceFocusConfig() {
        super(
        	true,
            "vengeanceFocus",
            null,
            VengeanceFocus.class
        );
    }
    
}
