package evilcraft.fluid;

import evilcraft.EvilCraft;
import org.cyclops.cyclopscore.config.extendedconfig.FluidConfig;

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
