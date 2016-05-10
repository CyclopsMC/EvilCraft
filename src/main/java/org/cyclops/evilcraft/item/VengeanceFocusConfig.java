package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

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
                EvilCraft._instance,
        	true,
            "vengeanceFocus",
            null,
            VengeanceFocus.class
        );
    }
    
}
