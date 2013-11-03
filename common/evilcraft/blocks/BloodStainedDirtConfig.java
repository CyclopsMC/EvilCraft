package evilcraft.blocks;

import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig;

public class BloodStainedDirtConfig extends ExtendedConfig {
    
    public static BloodStainedDirtConfig _instance;

    public BloodStainedDirtConfig() {
        super(
            Reference.BLOCK_BLOODSTAINEDDIRT,
            "Blood Stained Dirt",
            "bloodstaineddirt",
            null,
            BloodStainedDirt.class
        );
    }
    
}
