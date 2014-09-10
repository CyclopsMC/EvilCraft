package evilcraft.item;
import evilcraft.core.config.configurable.ConfigurableItem;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Blood infused {@link DarkGem}.
 * @author rubensworks
 *
 */
public class DarkPowerGem extends ConfigurableItem {
    
    private static DarkPowerGem _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new DarkPowerGem(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static DarkPowerGem getInstance() {
        return _instance;
    }

    private DarkPowerGem(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.maxStackSize = 16;
    }

}
