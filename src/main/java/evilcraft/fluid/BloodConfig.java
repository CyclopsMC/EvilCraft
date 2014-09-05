package evilcraft.fluid;

import evilcraft.core.config.FluidConfig;

/**
 * Config for {@link Blood}.
 * @author rubensworks
 *
 */
public class BloodConfig extends FluidConfig {
    
    /**
     * The unique instance.
     */
    public static BloodConfig _instance;

    /**
     * Make a new instance.
     */
    public BloodConfig() {
        super(
            true,
            "evilcraftblood",
            null,
            Blood.class
        );
    }
    
}
