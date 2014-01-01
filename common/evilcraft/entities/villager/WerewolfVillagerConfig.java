package evilcraft.entities.villager;

import java.util.Random;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.village.MerchantRecipeList;
import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageTradeHandler;
import evilcraft.api.config.ConfigHandler;
import evilcraft.api.config.ConfigurableProperty;
import evilcraft.api.config.ExtendedConfig;

public class WerewolfVillagerConfig extends ExtendedConfig implements IVillageTradeHandler {
    
    @ConfigurableProperty(category = ConfigHandler.CATEGORY_MOB, comment = "Should the Netherfish be enabled?")
    public static boolean isEnabled = true;
    
    public static WerewolfVillagerConfig _instance;

    public WerewolfVillagerConfig() {
        super(
            66666,
            "Werewolf Villager",
            "werewolfVillager",
            null,
            WerewolfVillager.class
        );
    }
    
    @Override
    public void onRegistered() {
        VillagerRegistry.instance().registerVillageTradeHandler(this.ID, this);
    }
    
    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
    
    @Override
    public void manipulateTradesForVillager(EntityVillager villager,
            MerchantRecipeList recipeList, Random random) {
        if (villager.getProfession() == this.ID) {
            // TODO: base on https://github.com/SlimeKnights/TinkersConstruct/blob/master/src/main/java/tconstruct/worldgen/village/TVillageTrades.java
        }
    }
    
}
