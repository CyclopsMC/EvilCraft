package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig;

public class LargeDoorItemConfig extends ExtendedConfig {
    
    public static LargeDoorItemConfig _instance;

    public LargeDoorItemConfig() {
        super(
            Reference.ITEM_LARGEDOOR,
            "Large Door",
            "largedooritem",
            null,
            LargeDoorItem.class
        );
    }
    
}
