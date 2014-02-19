package evilcraft.entities.villager;

import evilcraft.api.config.ElementTypeCategory;
import evilcraft.api.config.VillagerConfig;
import evilcraft.api.config.configurable.ConfigurableProperty;

/**
 * Config for the {@link WerewolfVillager}.
 * @author rubensworks
 *
 */
public class WerewolfVillagerConfig extends VillagerConfig {
    
    /**
     * The unique instance.
     */
    public static WerewolfVillagerConfig _instance;
    
    /**
     * Should the Netherfish be enabled?
     */
    @ConfigurableProperty(category = ElementTypeCategory.MOB, comment = "Should the Werewolf villager be enabled?")
    public static boolean isEnabled = true;

    /**
     * Make a new instance.
     */
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
