package evilcraft.block;

import evilcraft.EvilCraft;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;

/**
 * Config for the {@link evilcraft.block.DarkPowerGemBlock}.
 * @author rubensworks
 *
 */
public class DarkPowerGemBlockConfig extends BlockConfig {

    /**
     * The unique instance.
     */
    public static DarkPowerGemBlockConfig _instance;

    /**
     * Make a new instance.
     */
    public DarkPowerGemBlockConfig() {
        super(
                EvilCraft._instance,
        	true,
            "darkPowerGemBlock",
            null,
            DarkPowerGemBlock.class
        );
    }
    
    @Override
    public boolean isMultipartEnabled() {
        return true;
    }
    
}
