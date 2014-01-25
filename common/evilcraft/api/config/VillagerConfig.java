package evilcraft.api.config;

import java.util.Random;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.village.MerchantRecipeList;
import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageTradeHandler;
import evilcraft.api.config.configurable.ConfigurableVillager;

public abstract class VillagerConfig extends ExtendedConfig<VillagerConfig> {

    public VillagerConfig(int defaultId, String name, String namedId,
            String comment, Class<? extends ConfigurableVillager> element) {
        super(defaultId, name, namedId, comment, element);
    }
    

}
