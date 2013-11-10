package evilcraft.api.config.elementtypeaction;

import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraftforge.common.Configuration;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.IElementTypeAction;

public class MobAction extends IElementTypeAction{

    @Override
    public void run(ExtendedConfig eConfig, Configuration config) {
        // Save the config inside the correct element
        eConfig.save();
        
        // Register
        EntityRegistry.registerGlobalEntityID(
                eConfig.ELEMENT,
                eConfig.getSubUniqueName(),
                eConfig.ID, 3515848, 12102 // TODO: tmp egg
        );
        
        // Add I18N
        LanguageRegistry.instance().addStringLocalization("entity."+eConfig.NAMEDID+".name", eConfig.NAME);
    }

}
