package evilcraft.api.config;

import java.util.Random;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.village.MerchantRecipeList;
import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageTradeHandler;
import evilcraft.api.config.configurable.ConfigurableVillager;

public abstract class VillagerConfig extends ExtendedConfig<VillagerConfig> implements IVillageTradeHandler {

    public VillagerConfig(int defaultId, String name, String namedId,
            String comment, Class<? extends ConfigurableVillager> element) {
        super(defaultId, name, namedId, comment, element);
    }
    
    @Override
    public void onRegistered() {
        VillagerRegistry.instance().registerVillageTradeHandler(this.ID, this);
    }
    
    @Override
    public void manipulateTradesForVillager(EntityVillager villager,
            MerchantRecipeList recipeList, Random random) {
        if (villager.getProfession() == this.ID) {
            // TODO: base on https://github.com/SlimeKnights/TinkersConstruct/blob/master/src/main/java/tconstruct/worldgen/village/TVillageTrades.java
        }
    }
    

}
