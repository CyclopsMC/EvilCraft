package evilcraft.block;

import evilcraft.EvilCraft;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;

/**
 * Config for the {@link DarkBlock}.
 * @author rubensworks
 *
 */
public class DarkBlockConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static DarkBlockConfig _instance;

    /**
     * Make a new instance.
     */
    public DarkBlockConfig() {
        super(
                EvilCraft._instance,
        	true,
            "darkBlock",
            null,
            DarkBlock.class
        );
    }
    
    @Override
    public boolean isMultipartEnabled() {
        return true;
    }
    
}
