package evilcraft.fluids;

import evilcraft.api.config.ExtendedConfig;

public class BloodConfig extends ExtendedConfig {
    
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
    
    @Override
    public boolean isDisableable() {
        return false;
    }
    
}
