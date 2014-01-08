package evilcraft.api.config.elementtypeaction;

import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import evilcraft.EvilCraft;
import evilcraft.api.config.DummyConfig;

public class EntityAction extends IElementTypeAction<DummyConfig>{

    @Override
    public void preRun(DummyConfig eConfig, Configuration config) {
        if(!eConfig.isEnabled()) eConfig.ID = 0;
    }

    @Override
    public void postRun(DummyConfig eConfig, Configuration config) {
        // Save the config inside the correct element
        eConfig.save();
        
        // Register
        EntityRegistry.registerGlobalEntityID(
                eConfig.ELEMENT,
                eConfig.getSubUniqueName(),
                eConfig.ID
        );
        EntityRegistry.registerModEntity(
                eConfig.ELEMENT,
                eConfig.getSubUniqueName(),
                eConfig.ID, EvilCraft._instance, 160, 10, true);// TODO: make something nicer for these last three args?
        
        // Add I18N
        LanguageRegistry.instance().addStringLocalization("entity."+eConfig.NAMEDID+".name", eConfig.NAME);
    }

}
