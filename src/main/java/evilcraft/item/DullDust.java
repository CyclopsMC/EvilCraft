package evilcraft.item;
import evilcraft.core.config.configurable.ConfigurableItem;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Just a very boring pile of dust.
 * @author rubensworks
 *
 */
public class DullDust extends ConfigurableItem {

    private static DullDust _instance = null;

    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new DullDust(eConfig);
        else
            eConfig.showDoubleInitError();
    }

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static DullDust getInstance() {
        return _instance;
    }

    private DullDust(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
    }

}
