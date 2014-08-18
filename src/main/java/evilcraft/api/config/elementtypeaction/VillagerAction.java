package evilcraft.api.config.elementtypeaction;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.registry.VillagerRegistry;
import evilcraft.Reference;
import evilcraft.api.config.VillagerConfig;
import evilcraft.api.config.configurable.ConfigurableVillager;
import evilcraft.api.helpers.MinecraftHelpers;

/**
 * The action used for {@link VillagerConfig}.
 * @author rubensworks
 * @see IElementTypeAction
 */
public class VillagerAction extends IElementTypeAction<VillagerConfig>{

    @Override
    public void preRun(VillagerConfig eConfig, Configuration config, boolean startup) {
        if(startup && !eConfig.isEnabled()) eConfig.ID = 0;
    }

    @Override
    public void postRun(VillagerConfig eConfig, Configuration config) {
        // Save the config inside the correct element
        eConfig.save();
        
        // Register
        VillagerRegistry.instance().registerVillagerId(eConfig.ID);
        if (MinecraftHelpers.isClientSide()) {
            ResourceLocation villagerSkin = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_SKINS + eConfig.NAMEDID + ".png");
            VillagerRegistry.instance().registerVillagerSkin(eConfig.ID, villagerSkin);
        }
        
        // Add trades
        VillagerRegistry.instance().registerVillageTradeHandler(eConfig.ID, (ConfigurableVillager) eConfig.getSubInstance());
    }

}
