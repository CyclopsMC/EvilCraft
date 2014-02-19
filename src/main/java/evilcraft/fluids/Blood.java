package evilcraft.fluids;

import net.minecraftforge.fluids.Fluid;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.FluidConfig;
import evilcraft.api.config.configurable.ConfigurableFluid;

/**
 * The blood {@link Fluid}.
 * @author rubensworks
 *
 */
public class Blood extends ConfigurableFluid{
    
    private static Blood _instance = null;
    
    /**
     * Make the unique instance.
     * @param eConfig
     */
    public static void initInstance(ExtendedConfig<FluidConfig> eConfig) {
        if(_instance == null)
            _instance = new Blood(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The unique instance.
     */
    public static Blood getInstance() {
        return _instance;
    }

    private Blood(ExtendedConfig<FluidConfig> eConfig) {
        super(eConfig);
        setDensity(1500); // How tick the fluid is, affects movement inside the liquid.
        setViscosity(3000); // How fast the fluid flows.
        setTemperature(309); // 36 degrees C
    }

}
