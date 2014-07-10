package evilcraft.blocks;

import evilcraft.api.config.BlockConfig;

/**
 * Config for the {@link DarkBrick}.
 * @author rubensworks
 *
 */
public class DarkBrickConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static DarkBrickConfig _instance;

    /**
     * Make a new instance.
     */
    public DarkBrickConfig() {
        super(
        	true,
            "darkBrick",
            null,
            DarkBrick.class
        );
    }
    
}
