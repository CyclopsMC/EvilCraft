package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link DarkStick}.
 * @author rubensworks
 *
 */
public class DarkStickConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static DarkStickConfig _instance;

    /**
     * Make a new instance.
     */
    public DarkStickConfig() {
        super(
                EvilCraft._instance,
        	true,
            "darkStick",
            null,
            DarkStick.class
        );
    }
    
}
