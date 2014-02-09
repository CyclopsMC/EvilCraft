package evilcraft.blocks;

import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;

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
            Reference.BLOCK_FLUIDBLOCKBLOOD,
            "Blood",
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
