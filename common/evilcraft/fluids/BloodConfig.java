package evilcraft.fluids;

import evilcraft.api.config.FluidConfig;

public class BloodConfig extends FluidConfig {
    
    public static BloodConfig _instance;

    public BloodConfig() {
        super(
            1,
            "Blood",
            "blood",
            null,
            Blood.class
        );
    }
    
}
