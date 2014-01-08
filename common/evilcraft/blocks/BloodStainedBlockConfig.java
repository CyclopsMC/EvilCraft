package evilcraft.blocks;

import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;

public class BloodStainedBlockConfig extends BlockConfig {
    
    public static BloodStainedBlockConfig _instance;

    public BloodStainedBlockConfig() {
        super(
            Reference.BLOCK_BLOODSTAINEDDIRT,
            "Blood Stained Block",
            "bloodStainedBlock",
            null,
            BloodStainedBlock.class
        );
    }
    
    @Override
    public boolean hasSubTypes() {
        return true;
    }
    
}
