package evilcraft.blocks;

import evilcraft.api.config.BlockConfig;

/**
 * Config for the {@link UndeadLeaves}.
 * @author rubensworks
 *
 */
public class UndeadLeavesConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static UndeadLeavesConfig _instance;

    /**
     * Make a new instance.
     */
    public UndeadLeavesConfig() {
        super(
        	true,
            "undeadLeaves",
            null,
            UndeadLeaves.class
        );
    }
    
}
