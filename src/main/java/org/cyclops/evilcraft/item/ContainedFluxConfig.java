package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

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
                EvilCraft._instance,
        	true,
            "containedFlux",
            null,
            ContainedFlux.class
        );
    }
    
    @Override
    public boolean isHardDisabled() {
    	return true;
    }
    
}
