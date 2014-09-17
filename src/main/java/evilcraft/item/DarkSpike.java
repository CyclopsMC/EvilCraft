package evilcraft.item;
import evilcraft.core.config.configurable.ConfigurableItem;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Dark spike used in crafting.
 * @author rubensworks
 *
 */
public class DarkSpike extends ConfigurableItem {
    
    private static DarkSpike _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new DarkSpike(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static DarkSpike getInstance() {
        return _instance;
    }

    private DarkSpike(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
    }

}
