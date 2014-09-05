package evilcraft.fluid;

import evilcraft.core.config.FluidConfig;

/**
 * Config for {@link Poison}.
 * @author rubensworks
 *
 */
public class PoisonConfig extends FluidConfig {
    
    /**
     * The unique instance.
     */
    public static PoisonConfig _instance;

    /**
     * Make a new instance.
     */
    public PoisonConfig() {
        super(
            true,
            "evilcraftpoison",
            null,
            Poison.class
        );
    }
    
}
