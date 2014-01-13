package evilcraft.api.config.elementtypeaction;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import evilcraft.api.config.FluidConfig;

public class FluidAction extends IElementTypeAction<FluidConfig>{

    @Override
    public void preRun(FluidConfig eConfig, Configuration config) {
        eConfig.ID = 1;
    }

    @Override
    public void postRun(FluidConfig eConfig, Configuration config) {
        // Save the config inside the correct element
        eConfig.save();
        
        // Register
        FluidRegistry.registerFluid((Fluid) eConfig.getSubInstance());
        
        // Add I18N
        //LanguageRegistry.instance().addStringLocalization("fluid."+eConfig.NAMEDID+".name", eConfig.NAME);
    }

}
