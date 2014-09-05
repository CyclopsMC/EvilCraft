package evilcraft.block;

import evilcraft.core.config.BlockConfig;

/**
 * Config for {@link FluidBlockBlood}.
 * @author rubensworks
 *
 */
public class FluidBlockBloodConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static FluidBlockBloodConfig _instance;

    /**
     * Make a new instance.
     */
    public FluidBlockBloodConfig() {
        super(
        	true,
            "blockBlood",
            null,
            FluidBlockBlood.class
        );
    }
    
    @Override
    public boolean isDisableable() {
        return false;
    }
    
}
