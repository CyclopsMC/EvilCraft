package evilcraft.item;

import evilcraft.EvilCraft;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;

/**
 * Config for the {@link evilcraft.item.BloodPotash}.
 * @author rubensworks
 *
 */
public class BloodPotashConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static BloodPotashConfig _instance;

    /**
     * Make a new instance.
     */
    public BloodPotashConfig() {
        super(
                EvilCraft._instance,
        	true,
            "bloodPotash",
            null,
            BloodPotash.class
        );
    }
    
}
