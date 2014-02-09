package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ItemConfig;

/**
 * Config for the {@link ContainedFlux}.
 * @author rubensworks
 *
 */
public class ContainedFluxConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static ContainedFluxConfig _instance;

    /**
     * Make a new instance.
     */
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
