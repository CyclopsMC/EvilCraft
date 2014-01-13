package evilcraft.blocks;

import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;

public class BloodInfuserConfig extends BlockConfig {
    
    public static BloodInfuserConfig _instance;

    public BloodInfuserConfig() {
        super(
            Reference.BLOCK_BLOODINFUSER,
            "Blood Infuser",
            "bloodInfuser",
            null,
            BloodInfuser.class
        );
    }
    
}
