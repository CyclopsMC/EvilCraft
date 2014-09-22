package evilcraft.item;
import evilcraft.core.config.configurable.ConfigurableItem;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * A dark stick.
 * @author rubensworks
 *
 */
public class PotentiaSphere extends ConfigurableItem {
    
    private static PotentiaSphere _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new PotentiaSphere(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static PotentiaSphere getInstance() {
        return _instance;
    }

    private PotentiaSphere(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
    }

}
