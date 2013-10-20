package evilcraft.items;

import evilcraft.api.config.ExtendedConfig;

public class DarkGemConfig extends ExtendedConfig {
    
    public static DarkGemConfig _instance;

    public DarkGemConfig() {
        super(
            4005,
            "Dark Gem",
            "darkgem",
            null,
            DarkGem.class
        );
    }
    
}
