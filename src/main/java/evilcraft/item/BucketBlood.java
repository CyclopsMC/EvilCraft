package evilcraft.item;
import evilcraft.block.FluidBlockBlood;
import evilcraft.core.config.configurable.ConfigurableItemBucket;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;
import evilcraft.fluid.Blood;

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
        super(eConfig, FluidBlockBlood.getInstance());
    }

}
