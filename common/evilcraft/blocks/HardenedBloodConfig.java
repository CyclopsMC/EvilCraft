package evilcraft.blocks;

import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;

public class HardenedBloodConfig extends BlockConfig {
    
    public static HardenedBloodConfig _instance;

    public HardenedBloodConfig() {
        super(
            Reference.BLOCK_HARDENEDBLOOD,
            "Hardened Blood",
            "hardenedBlood",
            null,
            HardenedBlood.class
        );
    }
    
}
