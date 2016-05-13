package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link PiercingVengeanceFocus}.
 * @author rubensworks
 *
 */
public class PiercingVengeanceFocusConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static PiercingVengeanceFocusConfig _instance;

    /**
     * Make a new instance.
     */
    public PiercingVengeanceFocusConfig() {
        super(
                EvilCraft._instance,
        	true,
            "piercingVengeanceFocus",
            null,
            PiercingVengeanceFocus.class
        );
    }
    
}
