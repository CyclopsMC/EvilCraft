package evilcraft.blocks;

import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;

public class LiquidBlockBloodConfig extends BlockConfig {
    
    public static LiquidBlockBloodConfig _instance;

    public LiquidBlockBloodConfig() {
        super(
            Reference.BLOCK_LIQUIDBLOCKBLOOD,
            "Blood",
            "blockBlood",
            null,
            LiquidBlockBlood.class
        );
    }
    
    @Override
    public boolean isDisableable() {
        return false;
    }
    
}
