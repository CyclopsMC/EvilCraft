package evilcraft.blocks;

import evilcraft.api.config.ExtendedConfig;

public class EvilBlockConfig extends ExtendedConfig {
    
    public static EvilBlockConfig _instance;

    public EvilBlockConfig() {
        super(
            3841,
            "EvilBlock",
            "evilblock",
            null,
            EvilBlock.class
        );
    }
    
}
