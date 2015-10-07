package evilcraft.block;

import evilcraft.core.config.extendedconfig.BlockContainerConfig;

/**
 * Config for {@link FireBlaster}.
 * @author rubensworks
 *
 */
public class FireBlasterConfig extends BlockContainerConfig {

    /**
     * The unique instance.
     */
    public static FireBlasterConfig _instance;

    /**
     * Make a new instance.
     */
    public FireBlasterConfig() {
        super(
        	true,
            "fireBlaster",
            null,
            FireBlaster.class
        );
    }
    
}
