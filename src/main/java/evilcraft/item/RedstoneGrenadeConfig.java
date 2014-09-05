package evilcraft.item;

import evilcraft.core.config.ElementTypeCategory;
import evilcraft.core.config.ItemConfig;
import evilcraft.core.config.configurable.ConfigurableProperty;

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
     * If the redstone grenade should drop again as an item after it is being thrown.
     */
    @ConfigurableProperty(category = ElementTypeCategory.GENERAL, comment = "If the redstone grenade should drop again as an item after it is being thrown.", isCommandable = true)
    public static boolean dropAfterUsage = false;
    
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
