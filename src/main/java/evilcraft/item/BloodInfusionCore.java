package evilcraft.item;
import evilcraft.core.config.configurable.ConfigurableItem;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * A core used for infusion machine recipes.
 * @author rubensworks
 *
 */
public class BloodInfusionCore extends ConfigurableItem {
    
    private static BloodInfusionCore _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new BloodInfusionCore(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BloodInfusionCore getInstance() {
        return _instance;
    }

    private BloodInfusionCore(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
    }

}
