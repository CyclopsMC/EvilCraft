package evilcraft.item;
import evilcraft.block.HardenedBlood;
import evilcraft.core.config.configurable.ConfigurableItem;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Shard created from {@link HardenedBlood}.
 * @author rubensworks
 *
 */
public class HardenedBloodShard extends ConfigurableItem {
    
    private static HardenedBloodShard _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new HardenedBloodShard(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static HardenedBloodShard getInstance() {
        return _instance;
    }

    private HardenedBloodShard(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
    }

}
