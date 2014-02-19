package evilcraft.api.config.elementtypeaction;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import evilcraft.EvilCraft;
import evilcraft.Reference;
import evilcraft.api.config.EntityConfig;

/**
 * The action used for {@link EntityConfig}.
 * @author rubensworks
 * @see IElementTypeAction
 */
public class EntityAction extends IElementTypeAction<EntityConfig>{

    @Override
    public void preRun(EntityConfig eConfig, Configuration config) {
        if(!eConfig.isEnabled()) eConfig.ID = 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void postRun(EntityConfig eConfig, Configuration config) {
        // Save the config inside the correct element
        eConfig.save();
        
        // Register
        EntityRegistry.registerModEntity(
                eConfig.ELEMENT,
                eConfig.getSubUniqueName(),
                eConfig.ID,
                EvilCraft._instance,
                eConfig.getTrackingRange(),
                eConfig.getUpdateFrequency(),
                eConfig.sendVelocityUpdates()
        );;
    }

}
