package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig;

public class BroomConfig extends ExtendedConfig {
    
    public static BroomConfig _instance;

    public BroomConfig() {
        super(
            Reference.ITEM_BROOM,
            "Broom",
            "broomItem",
            null,
            Broom.class
        );
    }
    
}
