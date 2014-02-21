package evilcraft.items;

import evilcraft.api.config.ItemConfig;

/**
 * Config for the {@link RedstoneGrenade}.
 * @author immortaleeb
 *
 */
public class RedstoneGrenadeConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static RedstoneGrenadeConfig _instance;
    
    /**
     * Make a new instance.
     */
    public RedstoneGrenadeConfig() {
        super(
        		true,
                "redstoneGrenade",
                null,
                RedstoneGrenade.class
            );
    }
}
