package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ItemConfig;

public class BroomConfig extends ItemConfig {
    
    public static BroomConfig _instance;

    public BroomConfig() {
        super(
            Reference.ITEM_BROOM,
            "Broom",
            "broom",
            null,
            Broom.class
        );
    }
    
}
