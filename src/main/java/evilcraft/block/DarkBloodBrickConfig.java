package evilcraft.block;

import evilcraft.EvilCraft;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;

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
