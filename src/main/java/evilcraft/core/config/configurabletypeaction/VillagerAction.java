package evilcraft.core.config.configurabletypeaction;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.registry.VillagerRegistry;
import evilcraft.Reference;
import evilcraft.core.config.configurable.ConfigurableVillager;
import evilcraft.core.config.extendedconfig.VillagerConfig;
import evilcraft.core.helper.MinecraftHelpers;

/**
 * The action used for {@link VillagerConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class VillagerAction extends ConfigurableTypeAction<VillagerConfig>{

    @Override
    public void preRun(VillagerConfig eConfig, Configuration config, boolean startup) {
        if(startup && !eConfig.isEnabled()) eConfig.setId(0);
    }

    @Override
    public void postRun(VillagerConfig eConfig, Configuration config) {
        // Save the config inside the correct element
        eConfig.save();
        
        // Register
        VillagerRegistry.instance().registerVillagerId(eConfig.getId());
        if (MinecraftHelpers.isClientSide()) {
            ResourceLocation villagerSkin = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_SKINS + eConfig.getNamedId() + ".png");
            VillagerRegistry.instance().registerVillagerSkin(eConfig.getId(), villagerSkin);
        }
        
        // Add trades
        VillagerRegistry.instance().registerVillageTradeHandler(eConfig.getId(), (ConfigurableVillager) eConfig.getSubInstance());
    }

}
