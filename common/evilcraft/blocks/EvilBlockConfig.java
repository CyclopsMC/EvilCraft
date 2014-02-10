package evilcraft.blocks;

import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;

public class EvilBlockConfig extends BlockConfig {
    
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
    
    @Override
    public boolean isForceDisabled() {
        return true;
    }
    
}
