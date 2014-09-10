package evilcraft.item;
import evilcraft.core.config.configurable.ConfigurableItem;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Poisonous sac used in crafting.
 * @author rubensworks
 *
 */
public class PoisonSac extends ConfigurableItem {
    
    private static PoisonSac _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new PoisonSac(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static PoisonSac getInstance() {
        return _instance;
    }

    private PoisonSac(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
    }

}
