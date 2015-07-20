package org.cyclops.evilcraft.fluid;

import org.cyclops.cyclopscore.config.extendedconfig.FluidConfig;
import org.cyclops.evilcraft.EvilCraft;

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
                EvilCraft._instance,
                true,
                "evilcraftblood",
                null,
                Blood.class
        );
    }
    
}
