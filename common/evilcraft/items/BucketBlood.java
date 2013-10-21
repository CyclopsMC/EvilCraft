package evilcraft.items;
import evilcraft.EvilCraft;
import evilcraft.api.config.ConfigurableItemFluidContainer;
import evilcraft.api.config.ExtendedConfig;

public class BucketBlood extends ConfigurableItemFluidContainer {
    
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
        super(eConfig);
    }

}
