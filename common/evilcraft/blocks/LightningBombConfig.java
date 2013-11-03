package evilcraft.blocks;

import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig;

public class LightningBombConfig extends ExtendedConfig {
    
    public static LightningBombConfig _instance;

    public LightningBombConfig() {
        super(
            Reference.BLOCK_LIGHTNINGBOMB,
            "Lightning Bomb",
            "lightningbomb",
            null,
            LightningBomb.class
        );
    }
    
}
