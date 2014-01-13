package evilcraft.entities.villager;

import java.util.Random;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.village.MerchantRecipeList;
import cpw.mods.fml.common.registry.VillagerRegistry;
import evilcraft.api.config.ElementTypeCategory;
import evilcraft.api.config.VillagerConfig;
import evilcraft.api.config.configurable.ConfigurableProperty;

public class WerewolfVillagerConfig extends VillagerConfig {
    
    @ConfigurableProperty(category = ElementTypeCategory.MOB, comment = "Should the Werewolf villager be enabled?")
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
    public boolean isEnabled() {
        return isEnabled;
    }
    
}
