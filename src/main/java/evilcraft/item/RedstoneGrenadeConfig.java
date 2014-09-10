package evilcraft.item;

import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.extendedconfig.ItemConfig;

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
    @ConfigurableProperty(category = ConfigurableTypeCategory.GENERAL, comment = "If the redstone grenade should drop again as an item after it is being thrown.", isCommandable = true)
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
