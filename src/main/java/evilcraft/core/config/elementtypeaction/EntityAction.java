package evilcraft.core.config.elementtypeaction;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.registry.EntityRegistry;
import evilcraft.EvilCraft;
import evilcraft.core.config.EntityConfig;
import evilcraft.core.helper.Helpers;
import evilcraft.core.helper.Helpers.IDType;

/**
 * The action used for {@link EntityConfig}.
 * @author rubensworks
 * @see IElementTypeAction
 */
public class EntityAction extends IElementTypeAction<EntityConfig>{

    @Override
    public void preRun(EntityConfig eConfig, Configuration config, boolean startup) {
        
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
                Helpers.getNewId(IDType.ENTITY),
                EvilCraft._instance,
                eConfig.getTrackingRange(),
                eConfig.getUpdateFrequency(),
                eConfig.sendVelocityUpdates()
        );
    }

}
