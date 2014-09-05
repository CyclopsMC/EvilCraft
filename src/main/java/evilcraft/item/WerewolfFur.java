package evilcraft.item;
import evilcraft.core.config.ExtendedConfig;
import evilcraft.core.config.ItemConfig;
import evilcraft.core.config.configurable.ConfigurableItem;

/**
 * Fur of werewolves.
 * @author rubensworks
 *
 */
public class WerewolfFur extends ConfigurableItem {
    
    private static WerewolfFur _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new WerewolfFur(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static WerewolfFur getInstance() {
        return _instance;
    }

    private WerewolfFur(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
    }

}
