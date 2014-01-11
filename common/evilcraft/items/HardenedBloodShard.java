package evilcraft.items;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableItem;

public class HardenedBloodShard extends ConfigurableItem {
    
    private static HardenedBloodShard _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new HardenedBloodShard(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static HardenedBloodShard getInstance() {
        return _instance;
    }

    private HardenedBloodShard(ExtendedConfig eConfig) {
        super(eConfig);
    }

}
