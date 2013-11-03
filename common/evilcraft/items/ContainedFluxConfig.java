package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig;

public class ContainedFluxConfig extends ExtendedConfig {
    
    public static ContainedFluxConfig _instance;

    public ContainedFluxConfig() {
        super(
            Reference.ITEM_CONTAINEDFLUX,
            "Contained Flux",
            "containedflux",
            null,
            ContainedFlux.class
        );
    }
    
}
