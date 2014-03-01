package evilcraft.items;

import evilcraft.api.config.ItemConfig;

/**
 * Config for the {@link NeutronBlaster}.
 * @author rubensworks
 *
 */
public class NeutronBlasterConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static NeutronBlasterConfig _instance;

    /**
     * Make a new instance.
     */
    public NeutronBlasterConfig() {
        super(
        	true,
            "neutronBlaster",
            null,
            NeutronBlaster.class
        );
    }
    
}
