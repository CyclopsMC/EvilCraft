package evilcraft.items;

import evilcraft.api.config.ExtendedConfig;

public class DarkStickConfig extends ExtendedConfig {
    
    public static DarkStickConfig _instance;

    public DarkStickConfig() {
        super(
            4006,
            "Dark Stick",
            "darkstick",
            null,
            DarkStick.class
        );
    }
    
}
