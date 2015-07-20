package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

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
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "If the redstone grenade should drop again as an item after it is being thrown.", isCommandable = true)
    public static boolean dropAfterUsage = false;
    
    /**
     * Make a new instance.
     */
    public RedstoneGrenadeConfig() {
        super(
                EvilCraft._instance,
        		true,
                "redstoneGrenade",
                null,
                RedstoneGrenade.class
            );
    }
}
