package evilcraft.item;

import evilcraft.EvilCraft;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;

/**
 * Config for the Darkened Apple.
 * @author rubensworks
 *
 */
public class DarkenedAppleConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static DarkenedAppleConfig _instance;

    /**
     * Make a new instance.
     */
    public DarkenedAppleConfig() {
        super(
                EvilCraft._instance,
        	true,
            "darkenedApple",
            null,
            DarkenedApple.class
        );
    }
    
}
