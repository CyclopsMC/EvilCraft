package evilcraft.api.config.elementtypeaction;

import net.minecraft.entity.EntityLiving;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import evilcraft.EvilCraft;
import evilcraft.Reference;
import evilcraft.api.Helpers;
import evilcraft.api.config.MobConfig;
import evilcraft.proxies.ClientProxy;

/**
 * The action used for {@link MobConfig}.
 * @author rubensworks
 * @see IElementTypeAction
 */
public class MobAction extends IElementTypeAction<MobConfig>{

    @Override
    public void preRun(MobConfig eConfig, Configuration config) {
        if(!eConfig.isEnabled()) eConfig.ID = 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void postRun(MobConfig eConfig, Configuration config) {
        // Save the config inside the correct element
        eConfig.save();
        
        // Register mob
        Class<? extends EntityLiving> clazz = (Class<? extends EntityLiving>) eConfig.ELEMENT;
        if (Helpers.isClientSide())
            ClientProxy.ENTITY_RENDERERS.put(clazz, eConfig.getRender());
        EntityRegistry.registerGlobalEntityID(clazz, eConfig.NAMEDID, EntityRegistry.findGlobalUniqueEntityId(), eConfig.getBackgroundEggColor(), eConfig.getForegroundEggColor());
        EntityRegistry.registerModEntity(clazz, eConfig.NAMEDID, eConfig.ID, EvilCraft._instance, 80, 3, true);
        
        // Add I18N
        LanguageRegistry.instance().addStringLocalization("entity." + Reference.MOD_ID + ".instance." + eConfig.NAMEDID + ".name", eConfig.NAME);
        LanguageRegistry.instance().addStringLocalization("entity." + Reference.MOD_ID + "." + eConfig.NAMEDID + ".name", eConfig.NAME);
        LanguageRegistry.instance().addStringLocalization("entity." + eConfig.NAMEDID + ".name", eConfig.NAME); // For global registering
    }

}
