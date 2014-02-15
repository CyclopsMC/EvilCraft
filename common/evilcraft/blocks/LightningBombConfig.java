package evilcraft.blocks;

import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;

/**
 * Config for the {@link LightningBomb}.
 * @author rubensworks
 *
 */
public class LightningBombConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static LightningBombConfig _instance;

    /**
     * Make a new instance.
     */
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
