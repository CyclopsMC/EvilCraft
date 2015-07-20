package org.cyclops.evilcraft.item;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;

/**
 * A dark stick.
 * @author rubensworks
 *
 */
public class DarkStick extends ConfigurableItem {
    
    private static DarkStick _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static DarkStick getInstance() {
        return _instance;
    }

    public DarkStick(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
    }

}
