package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig;

public class BloodPearlOfTeleportationConfig extends ExtendedConfig {
    
    public static BloodPearlOfTeleportationConfig _instance;

    public BloodPearlOfTeleportationConfig() {
        super(
            Reference.ITEM_WEATHERCONTAINER,
            "Blood Pearl of Teleportation",
            "bloodPearlOfTeleportation",
            null,
            BloodPearlOfTeleportation.class
        );
    }
    
}
