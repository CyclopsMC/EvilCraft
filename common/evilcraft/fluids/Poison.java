package evilcraft.fluids;

import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableFluid;

public class Poison extends ConfigurableFluid{
    
    private static Poison _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new Poison(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static Poison getInstance() {
        return _instance;
    }

    private Poison(ExtendedConfig eConfig) {
        super(eConfig);
        setDensity(1000); // How tick the fluid is, affects movement inside the liquid.
        setViscosity(1000); // How fast the fluid flows.
        setTemperature(290); // 36 degrees C
    }

}
