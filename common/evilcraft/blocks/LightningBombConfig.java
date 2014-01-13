package evilcraft.blocks;

import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;

public class LightningBombConfig extends BlockConfig {
    
    public static LightningBombConfig _instance;

    public LightningBombConfig() {
        super(
            Reference.BLOCK_LIGHTNINGBOMB,
            "Lightning Bomb",
            "lightningBomb",
            null,
            LightningBomb.class
        );
    }
    
}
