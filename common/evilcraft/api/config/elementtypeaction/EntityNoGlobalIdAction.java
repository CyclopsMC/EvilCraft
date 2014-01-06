package evilcraft.api.config.elementtypeaction;

import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.registry.EntityRegistry;
import evilcraft.EvilCraft;
import evilcraft.api.config.ExtendedConfig;

public class EntityNoGlobalIdAction extends IElementTypeAction{

    @Override
    public void preRun(ExtendedConfig eConfig, Configuration config) {
        if(!eConfig.isEnabled()) eConfig.ID = 0;
    }

    @Override
    public void postRun(ExtendedConfig eConfig, Configuration config) {
    	EvilCraft.log("postRun EntityNoGlobalId");
        // Save the config inside the correct element
        eConfig.save();
        
        // Register
        EntityRegistry.registerModEntity(eConfig.ELEMENT, eConfig.NAMEDID, eConfig.ID, EvilCraft._instance, 80, 3, true);
    }

}
