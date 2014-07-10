package evilcraft.blocks;

import evilcraft.api.config.BlockConfig;

/**
 * Config for the {@link DarkBloodBrick}.
 * @author rubensworks
 *
 */
public class DarkBloodBrickConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static DarkBloodBrickConfig _instance;

    /**
     * Make a new instance.
     */
    public DarkBloodBrickConfig() {
        super(
        	true,
            "darkBloodBrick",
            null,
            DarkBloodBrick.class
        );
    }
    
}
