package evilcraft.item;
import evilcraft.core.config.configurable.ConfigurableItem;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * An enderman's tear.
 * @author rubensworks
 *
 */
public class EnderTear extends ConfigurableItem {

    private static EnderTear _instance = null;

    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new EnderTear(eConfig);
        else
            eConfig.showDoubleInitError();
    }

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static EnderTear getInstance() {
        return _instance;
    }

    private EnderTear(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
    }

}
