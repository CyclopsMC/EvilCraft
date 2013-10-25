package evilcraft.blocks;

import evilcraft.api.config.ExtendedConfig;

public class LightningBombConfig extends ExtendedConfig {
    
    public static LightningBombConfig _instance;

    public LightningBombConfig() {
        super(
            3847,
            "Lightning Bomb",
            "lightningbomb",
            null,
            LightningBomb.class
        );
    }
    
}
