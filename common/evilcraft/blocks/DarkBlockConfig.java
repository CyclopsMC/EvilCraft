package evilcraft.blocks;

import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig;

public class DarkBlockConfig extends ExtendedConfig {
    
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
