package evilcraft.items;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableItemBucket;
import evilcraft.blocks.FluidBlockPoison;

public class BucketPoison extends ConfigurableItemBucket {
    
    private static BucketPoison _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new BucketPoison(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static BucketPoison getInstance() {
        return _instance;
    }

    private BucketPoison(ExtendedConfig eConfig) {
        super(eConfig, FluidBlockPoison.getInstance().blockID);
    }

}
