package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig;

public class DarkGemConfig extends ExtendedConfig {
    
    public static DarkGemConfig _instance;

    public DarkGemConfig() {
        super(
            Reference.ITEM_DARKGEM,
            "Dark Gem",
            "darkgem",
            null,
            DarkGem.class
        );
    }
    
}
