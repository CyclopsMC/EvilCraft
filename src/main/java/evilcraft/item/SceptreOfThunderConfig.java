package evilcraft.item;

import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Config for the {@link SceptreOfThunder}.
 * @author rubensworks
 *
 */
public class SceptreOfThunderConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static SceptreOfThunderConfig _instance;

    /**
     * Make a new instance.
     */
    public SceptreOfThunderConfig() {
        super(
        	true,
            "sceptreOfThunder",
            null,
            SceptreOfThunder.class
        );
    }
    
}
