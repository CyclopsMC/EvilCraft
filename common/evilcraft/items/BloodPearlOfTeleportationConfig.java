package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ItemConfig;

public class BloodPearlOfTeleportationConfig extends ItemConfig {
    
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
    
    @Override
    public boolean blendAlpha() {
        return true;
    }
    
}
