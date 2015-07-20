package org.cyclops.evilcraft.item;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;

/**
 * Contained flux.
 * @author rubensworks
 *
 */
public class ContainedFlux extends ConfigurableItem {
    
    private static ContainedFlux _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static ContainedFlux getInstance() {
        return _instance;
    }

    public ContainedFlux(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.maxStackSize = 1;
    }

}
