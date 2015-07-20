package org.cyclops.evilcraft.fluid;

import net.minecraftforge.fluids.Fluid;
import org.cyclops.cyclopscore.config.configurable.ConfigurableFluid;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.FluidConfig;

/**
 * The blood {@link Fluid}.
 * @author rubensworks
 *
 */
public class Blood extends ConfigurableFluid {
    
    private static Blood _instance = null;
    
    /**
     * Get the unique instance.
     * @return The unique instance.
     */
    public static Blood getInstance() {
        return _instance;
    }

    public Blood(ExtendedConfig<FluidConfig> eConfig) {
        super(eConfig);
        setDensity(1500); // How tick the fluid is, affects movement inside the liquid.
        setViscosity(3000); // How fast the fluid flows.
        setTemperature(309); // 36 degrees C
    }

}
