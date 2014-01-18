package evilcraft.items;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableItem;

public class BloodInfusionCore extends ConfigurableItem {
    
    private static BloodInfusionCore _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new BloodInfusionCore(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static BloodInfusionCore getInstance() {
        return _instance;
    }

    private BloodInfusionCore(ExtendedConfig eConfig) {
        super(eConfig);
    }

}
