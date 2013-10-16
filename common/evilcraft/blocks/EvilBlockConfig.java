package evilcraft.blocks;

import evilcraft.api.config.ExtendedConfig;

public class EvilBlockConfig extends ExtendedConfig {
    
    public static EvilBlockConfig _instance;

    public EvilBlockConfig() {
        super(
            257,
            "EvilBlock",
            "evilblock",
            null,
            EvilBlock.class
        );
    }
    
}
