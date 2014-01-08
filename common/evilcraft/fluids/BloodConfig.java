package evilcraft.fluids;

import evilcraft.api.config.DummyConfig;

public class BloodConfig extends DummyConfig {
    
    public static BloodConfig _instance;

    public BloodConfig() {
        super(
            1,
            "Blood",
            "Blood",
            null,
            Blood.class
        );
    }
    
    @Override
    public boolean isDisableable() {
        return false;
    }
    
}
