package evilcraft.items;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableItem;

public class DarkPowerGem extends ConfigurableItem {
    
    private static DarkPowerGem _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new DarkPowerGem(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static DarkPowerGem getInstance() {
        return _instance;
    }

    private DarkPowerGem(ExtendedConfig eConfig) {
        super(eConfig);
        this.maxStackSize = 16;
    }

}
