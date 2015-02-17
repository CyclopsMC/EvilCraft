package evilcraft.modcompat.thaumcraft;

import evilcraft.core.config.configurable.ConfigurableItem;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Blood Infused Golden Wand cap that has a slightly higher discount.
 * Textures are based on the ones from Thaumcraft.
 * @author rubensworks
 *
 */
public class BloodWandCap extends ConfigurableItem {

    private static BloodWandCap _instance = null;
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new BloodWandCap(eConfig);
        else
            eConfig.showDoubleInitError();
    }

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BloodWandCap getInstance() {
        return _instance;
    }

    private BloodWandCap(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
    }

}
