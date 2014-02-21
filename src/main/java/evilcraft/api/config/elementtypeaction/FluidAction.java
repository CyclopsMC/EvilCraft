package evilcraft.api.config.elementtypeaction;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import evilcraft.api.config.FluidConfig;

/**
 * The action used for {@link FluidConfig}.
 * @author rubensworks
 * @see IElementTypeAction
 */
public class FluidAction extends IElementTypeAction<FluidConfig>{

    @Override
    public void preRun(FluidConfig eConfig, Configuration config) {
        
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
