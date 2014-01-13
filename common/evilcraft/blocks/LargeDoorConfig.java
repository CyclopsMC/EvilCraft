package evilcraft.blocks;

import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;

public class LargeDoorConfig extends BlockConfig {
    
    public static LargeDoorConfig _instance;

    public LargeDoorConfig() {
        super(
            Reference.BLOCK_LARGEDOOR,
            "Large Door",
            "largeDoor",
            null,
            LargeDoor.class
        );
    }
    
}
