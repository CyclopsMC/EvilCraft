package evilcraft.blocks;

import evilcraft.api.config.ExtendedConfig;

public class DarkBlockConfig extends ExtendedConfig {
    
    public static DarkBlockConfig _instance;

    public DarkBlockConfig() {
        super(
            3845,
            "Dark Block",
            "darkblock",
            null,
            DarkBlock.class
        );
    }
    
}
