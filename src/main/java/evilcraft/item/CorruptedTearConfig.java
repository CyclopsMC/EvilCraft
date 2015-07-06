package evilcraft.item;

import evilcraft.EvilCraft;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;

/**
 * Config for the Corrupted Tear.
 * @author rubensworks
 *
 */
public class CorruptedTearConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static CorruptedTearConfig _instance;

    /**
     * Make a new instance.
     */
    public CorruptedTearConfig() {
        super(
                EvilCraft._instance,
            true,
            "corruptedTear",
            null,
            null
        );
    }
    
}
