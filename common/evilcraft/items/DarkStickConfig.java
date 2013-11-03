package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig;

public class DarkStickConfig extends ExtendedConfig {
    
    public static DarkStickConfig _instance;

    public DarkStickConfig() {
        super(
            Reference.ITEM_DARKSTICK,
            "Dark Stick",
            "darkstick",
            null,
            DarkStick.class
        );
    }
    
}
