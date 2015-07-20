package org.cyclops.evilcraft.block;

import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

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
                EvilCraft._instance,
        	true,
            "darkBloodBrick",
            null,
            DarkBloodBrick.class
        );
    }
    
    @Override
    public boolean isMultipartEnabled() {
        return true;
    }
    
}
