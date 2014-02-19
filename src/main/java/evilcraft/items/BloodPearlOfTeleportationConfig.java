package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ItemConfig;

/**
 * Config for the {@link BloodPearlOfTeleportation}.
 * @author rubensworks
 *
 */
public class BloodPearlOfTeleportationConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static BloodPearlOfTeleportationConfig _instance;

    /**
     * Make a new instance.
     */
    public BloodPearlOfTeleportationConfig() {
        super(
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
