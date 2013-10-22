package evilcraft.items;
import evilcraft.EvilCraft;
import evilcraft.api.config.ConfigurableItemBucket;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.blocks.LiquidBlockBlood;

public class BucketBlood extends ConfigurableItemBucket {
    
    private static BucketBlood _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new BucketBlood(eConfig);
        else EvilCraft.log("If you see this, something went horribly wrong while registring stuff!");
    }
    
    public static BucketBlood getInstance() {
        return _instance;
    }

    private BucketBlood(ExtendedConfig eConfig) {
        super(eConfig, LiquidBlockBlood.getInstance().blockID);
    }

}
