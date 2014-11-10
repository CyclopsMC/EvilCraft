package evilcraft.item;
import evilcraft.core.config.configurable.ConfigurableItem;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * A corrupted tear.
 * @author rubensworks
 *
 */
public class CorruptedTear extends ConfigurableItem {

    private static CorruptedTear _instance = null;

    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new CorruptedTear(eConfig);
        else
            eConfig.showDoubleInitError();
    }

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static CorruptedTear getInstance() {
        return _instance;
    }

    private CorruptedTear(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
    }

}
