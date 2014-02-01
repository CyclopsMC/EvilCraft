package evilcraft.fluids;

import evilcraft.api.config.FluidConfig;

public class PoisonConfig extends FluidConfig {
    
    public static PoisonConfig _instance;

    public PoisonConfig() {
        super(
            1,
            "Poison",
            "poison",
            null,
            Poison.class
        );
    }
    
}
