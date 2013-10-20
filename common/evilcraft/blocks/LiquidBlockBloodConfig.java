package evilcraft.blocks;

import evilcraft.api.config.ExtendedConfig;

public class LiquidBlockBloodConfig extends ExtendedConfig {
    
    public static LiquidBlockBloodConfig _instance;

    public LiquidBlockBloodConfig() {
        super(
            3842,
            "Blood",
            "blockblood",
            null,
            LiquidBlockBlood.class
        );
    }
    
}
