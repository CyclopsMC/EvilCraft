package evilcraft.items;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.ItemConfig;
import evilcraft.api.config.configurable.ConfigurableItemBucket;
import evilcraft.blocks.FluidBlockPoison;
import evilcraft.fluids.Poison;

/**
 * Bucket for {@link Poison}.
 * @author rubensworks
 *
 */
public class BucketPoison extends ConfigurableItemBucket {
    
    private static BucketPoison _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new BucketPoison(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BucketPoison getInstance() {
        return _instance;
    }

    private BucketPoison(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig, FluidBlockPoison.getInstance().blockID);
    }

}
