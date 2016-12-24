package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link VengeancePickaxe}.
 * @author rubensworks
 *
 */
public class VengeancePickaxeConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static VengeancePickaxeConfig _instance;

    /**
     * Make a new instance.
     */
    public VengeancePickaxeConfig() {
        super(
                EvilCraft._instance,
        	true,
            "vengeance_pickaxe",
            null,
            VengeancePickaxe.class
        );
    }
    
}
