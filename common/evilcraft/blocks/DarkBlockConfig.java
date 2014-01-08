package evilcraft.blocks;

import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;

public class DarkBlockConfig extends BlockConfig {
    
    public static DarkBlockConfig _instance;

    public DarkBlockConfig() {
        super(
            Reference.BLOCK_DARKBLOCK,
            "Dark Block",
            "darkBlock",
            null,
            DarkBlock.class
        );
    }
    
}
