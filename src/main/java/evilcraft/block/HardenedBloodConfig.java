package evilcraft.block;

import evilcraft.core.config.extendedconfig.BlockConfig;

/**
 * A config for {@link HardenedBlood}.
 * @author rubensworks
 *
 */
public class HardenedBloodConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static HardenedBloodConfig _instance;

    /**
     * Make a new instance.
     */
    public HardenedBloodConfig() {
        super(
        	true,
            "hardenedBlood",
            null,
            HardenedBlood.class
        );
    }
    
    @Override
    public boolean isMultipartEnabled() {
        return true;
    }
    
    @Override
    public boolean isDisableable() {
        return false;
    }
    
}
