package evilcraft.item;

import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Config for the {@link evilcraft.item.CorruptedTear}.
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
            true,
            "corruptedTear",
            null,
            CorruptedTear.class
        );
    }
    
}
