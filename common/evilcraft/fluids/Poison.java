package evilcraft.fluids;

import net.minecraftforge.fluids.Fluid;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.FluidConfig;
import evilcraft.api.config.configurable.ConfigurableFluid;

/**
 * The poisonous {@link Fluid}.
 * @author rubensworks
 *
 */
public class Poison extends ConfigurableFluid{
    
    private static Poison _instance = null;
    
    /**
     * Make the unique instance.
     * @param eConfig
     */
    public static void initInstance(ExtendedConfig<FluidConfig> eConfig) {
        if(_instance == null)
            _instance = new Poison(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The unique instance.
     */
    public static Poison getInstance() {
        return _instance;
    }

    private Poison(ExtendedConfig<FluidConfig> eConfig) {
        super(eConfig);
        setDensity(1000); // How tick the fluid is, affects movement inside the liquid.
        setViscosity(1000); // How fast the fluid flows.
        setTemperature(290); // 36 degrees C
    }

}
