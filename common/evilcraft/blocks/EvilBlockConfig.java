package evilcraft.blocks;

import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig;

public class EvilBlockConfig extends ExtendedConfig {
    
    public static EvilBlockConfig _instance;

    public EvilBlockConfig() {
        super(
            Reference.BLOCK_EVILBLOCK,
            "EvilBlock",
            "evilBlock",
            null,
            EvilBlock.class
        );
    }
    
}
