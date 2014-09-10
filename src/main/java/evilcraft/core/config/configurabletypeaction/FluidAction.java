package evilcraft.core.config.configurabletypeaction;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import evilcraft.core.config.extendedconfig.FluidConfig;

/**
 * The action used for {@link FluidConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class FluidAction extends ConfigurableTypeAction<FluidConfig>{

    @Override
    public void preRun(FluidConfig eConfig, Configuration config, boolean startup) {
        
    }

    @Override
    public void postRun(FluidConfig eConfig, Configuration config) {
        // Save the config inside the correct element
        eConfig.save();
        
        // Register
        FluidRegistry.registerFluid((Fluid) eConfig.getSubInstance());
        
        // Add I18N
        //LanguageRegistry.instance().addStringLocalization("fluid.fluids."+eConfig.NAMEDID, eConfig.NAME);
    }

}
