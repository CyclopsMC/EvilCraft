package evilcraft.blocks;

import evilcraft.api.config.ExtendedConfig;

public class BloodStainedDirtConfig extends ExtendedConfig {
    
    public static BloodStainedDirtConfig _instance;

    public BloodStainedDirtConfig() {
        super(
            3846,
            "Blood Stained Dirt",
            "bloodstaineddirt",
            null,
            BloodStainedDirt.class
        );
    }
    
}
