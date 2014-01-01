package evilcraft.api.config.elementtypeaction;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.registry.VillagerRegistry;
import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig;

public class VillagerAction extends IElementTypeAction{

    @Override
    public void preRun(ExtendedConfig eConfig, Configuration config) {
        if(!eConfig.isEnabled()) eConfig.ID = 0;
    }

    @Override
    public void postRun(ExtendedConfig eConfig, Configuration config) {
        // Save the config inside the correct element
        eConfig.save();
        
        // Register
        VillagerRegistry.instance().registerVillagerId(eConfig.ID);
        ResourceLocation villagerSkin = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_SKINS + eConfig.NAMEDID + ".png");
        VillagerRegistry.instance().registerVillagerSkin(eConfig.ID, villagerSkin);
    }

}
