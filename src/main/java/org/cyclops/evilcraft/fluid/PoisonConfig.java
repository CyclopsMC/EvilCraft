package org.cyclops.evilcraft.fluid;


import org.cyclops.cyclopscore.config.extendedconfig.FluidConfig;
import org.cyclops.evilcraft.EvilCraft;

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
                EvilCraft._instance,
            true,
            "evilcraftpoison",
            null,
            Poison.class
        );
    }
    
}
