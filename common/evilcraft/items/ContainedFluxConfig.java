package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ItemConfig;

public class ContainedFluxConfig extends ItemConfig {
    
    public static ContainedFluxConfig _instance;

    public ContainedFluxConfig() {
        super(
            Reference.ITEM_CONTAINEDFLUX,
            "Contained Flux",
            "containedFlux",
            null,
            ContainedFlux.class
        );
    }
    
    @Override
    public boolean isForceDisabled() {
        return true;
    }
    
}
