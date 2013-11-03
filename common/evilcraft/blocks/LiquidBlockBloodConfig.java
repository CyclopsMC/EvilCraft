package evilcraft.blocks;

import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig;

public class LiquidBlockBloodConfig extends ExtendedConfig {
    
    public static LiquidBlockBloodConfig _instance;

    public LiquidBlockBloodConfig() {
        super(
            Reference.BLOCK_LIQUIDBLOCKBLOOD,
            "Blood",
            "blockblood",
            null,
            LiquidBlockBlood.class
        );
    }
    
}
