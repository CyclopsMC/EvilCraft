package evilcraft.core.config.elementtypeaction;

import net.minecraft.entity.EntityLiving;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.registry.EntityRegistry;
import evilcraft.EvilCraft;
import evilcraft.core.config.MobConfig;
import evilcraft.core.helper.Helpers;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.core.helper.Helpers.IDType;
import evilcraft.proxy.ClientProxy;

/**
 * The action used for {@link MobConfig}.
 * @author rubensworks
 * @see IElementTypeAction
 */
public class MobAction extends IElementTypeAction<MobConfig>{

    @Override
    public void preRun(MobConfig eConfig, Configuration config, boolean startup) {
        
    }

    @SuppressWarnings("unchecked")
    @Override
    public void postRun(MobConfig eConfig, Configuration config) {
        // Save the config inside the correct element
        eConfig.save();
        
        // Register mob
        Class<? extends EntityLiving> clazz = (Class<? extends EntityLiving>) eConfig.ELEMENT;
        if (MinecraftHelpers.isClientSide())
            ClientProxy.ENTITY_RENDERERS.put(clazz, eConfig.getRender());
        EntityRegistry.registerGlobalEntityID(clazz, eConfig.NAMEDID, EntityRegistry.findGlobalUniqueEntityId(), eConfig.getBackgroundEggColor(), eConfig.getForegroundEggColor());
		EntityRegistry.registerModEntity(clazz, eConfig.NAMEDID, Helpers.getNewId(IDType.ENTITY), EvilCraft._instance, 80, 3, true);
    }

}
