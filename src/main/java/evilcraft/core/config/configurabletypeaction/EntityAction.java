package evilcraft.core.config.configurabletypeaction;

import evilcraft.EvilCraft;
import evilcraft.core.config.extendedconfig.EntityConfig;
import evilcraft.core.helper.Helpers;
import evilcraft.core.helper.Helpers.IDType;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.EntityRegistry;

/**
 * The action used for {@link EntityConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class EntityAction extends ConfigurableTypeAction<EntityConfig>{

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
                eConfig.getElement(),
                eConfig.getSubUniqueName(),
                Helpers.getNewId(IDType.ENTITY),
                EvilCraft._instance,
                eConfig.getTrackingRange(),
                eConfig.getUpdateFrequency(),
                eConfig.sendVelocityUpdates()
        );
    }

}
