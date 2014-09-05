package evilcraft.items;
import evilcraft.core.config.ExtendedConfig;
import evilcraft.core.config.ItemConfig;
import evilcraft.core.config.configurable.ConfigurableItem;

/**
 * Contained flux.
 * @author rubensworks
 *
 */
public class ContainedFlux extends ConfigurableItem {
    
    private static ContainedFlux _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new ContainedFlux(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static ContainedFlux getInstance() {
        return _instance;
    }

    private ContainedFlux(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.maxStackSize = 1;
    }

}
