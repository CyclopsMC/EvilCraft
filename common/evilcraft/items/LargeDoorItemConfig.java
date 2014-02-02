package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ItemConfig;

public class LargeDoorItemConfig extends ItemConfig {
    
    public static LargeDoorItemConfig _instance;

    public LargeDoorItemConfig() {
        super(
            Reference.ITEM_LARGEDOOR,
            "Large Door",
            "largeDoorItem",
            null,
            LargeDoorItem.class
        );
    }
    
    public boolean isForceDisabled() {
        return true;
    }
    
}
