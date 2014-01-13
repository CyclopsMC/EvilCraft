package evilcraft.blocks;

import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;

public class FluidBlockBloodConfig extends BlockConfig {
    
    public static FluidBlockBloodConfig _instance;

    public FluidBlockBloodConfig() {
        super(
            Reference.BLOCK_LIQUIDBLOCKBLOOD,
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
