package evilcraft.core.config.configurabletypeaction;

import evilcraft.Reference;
import evilcraft.core.config.configurable.ConfigurableVillager;
import evilcraft.core.config.extendedconfig.VillagerConfig;
import evilcraft.core.helper.MinecraftHelpers;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

/**
 * The action used for {@link VillagerConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class VillagerAction extends ConfigurableTypeAction<VillagerConfig>{

    @Override
    public void preRun(VillagerConfig eConfig, Configuration config, boolean startup) {
        //if(startup && !eConfig.isEnabled()) eConfig.setId(0);
    }

    @Override
    public void postRun(VillagerConfig eConfig, Configuration config) {
        // Save the config inside the correct element
        eConfig.save();
        
        // Register
        String name = Reference.MOD_ID + ":" + eConfig.getNamedId();
        String resource = Reference.MOD_ID + ":" + Reference.TEXTURE_PATH_SKINS + eConfig.getNamedId() + ".png";
        VillagerRegistry.VillagerProfession profession = new VillagerRegistry.VillagerProfession(name, resource);
        VillagerRegistry.instance().register(profession);

        // TODO: This is still being written in Forge!!!
        
        // Add trades
        //VillagerRegistry.instance().registerVillageTradeHandler(eConfig.getId(), (ConfigurableVillager) eConfig.getSubInstance());
    }

}
