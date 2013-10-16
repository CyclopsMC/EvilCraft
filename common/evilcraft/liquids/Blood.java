package evilcraft.liquids;

import evilcraft.EvilCraft;
import evilcraft.api.config.ConfigurableFluid;
import evilcraft.api.config.ExtendedConfig;

public class Blood extends ConfigurableFluid{
    
    private static Blood _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new Blood(eConfig);
        else EvilCraft.log("If you see this, something went horribly wrong while registring stuff!");
    }
    
    public static Blood getInstance() {
        return _instance;
    }

    private Blood(ExtendedConfig eConfig) {
        super(eConfig);
        setDensity(10); // How tick the fluid is, affects movement inside the liquid.
        setViscosity(10); // How fast the fluid flows.
    }

}
