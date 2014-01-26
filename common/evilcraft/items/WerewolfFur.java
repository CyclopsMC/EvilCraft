package evilcraft.items;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableItem;

public class WerewolfFur extends ConfigurableItem {
    
    private static WerewolfFur _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new WerewolfFur(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static WerewolfFur getInstance() {
        return _instance;
    }

    private WerewolfFur(ExtendedConfig eConfig) {
        super(eConfig);
    }

}
