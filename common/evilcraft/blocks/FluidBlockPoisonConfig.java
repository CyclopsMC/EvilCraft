package evilcraft.blocks;

import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;

public class FluidBlockPoisonConfig extends BlockConfig {
    
    public static FluidBlockPoisonConfig _instance;

    public FluidBlockPoisonConfig() {
        super(
            Reference.BLOCK_FLUIDBLOCKPOISON,
            "Poison",
            "blockPoison",
            null,
            FluidBlockPoison.class
        );
    }
    
    @Override
    public boolean isDisableable() {
        return false;
    }
    
}
