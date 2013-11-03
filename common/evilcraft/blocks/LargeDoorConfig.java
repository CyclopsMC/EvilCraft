package evilcraft.blocks;

import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig;

public class LargeDoorConfig extends ExtendedConfig {
    
    public static LargeDoorConfig _instance;

    public LargeDoorConfig() {
        super(
            Reference.BLOCK_LARGEDOOR,
            "Large Door",
            "largedoor",
            null,
            LargeDoor.class
        );
    }
    
}
