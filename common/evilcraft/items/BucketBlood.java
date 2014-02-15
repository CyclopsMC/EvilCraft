package evilcraft.items;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.ItemConfig;
import evilcraft.api.config.configurable.ConfigurableItemBucket;
import evilcraft.blocks.FluidBlockBlood;
import evilcraft.fluids.Blood;

/**
 * Bucket for {@link Blood}.
 * @author rubensworks
 *
 */
public class BucketBlood extends ConfigurableItemBucket {
    
    private static BucketBlood _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new BucketBlood(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BucketBlood getInstance() {
        return _instance;
    }

    private BucketBlood(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig, FluidBlockBlood.getInstance().blockID);
    }

}
