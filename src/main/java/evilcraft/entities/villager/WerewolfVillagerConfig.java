package evilcraft.entities.villager;

import evilcraft.Reference;
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
     * The id of the Werewolf villager
     */
    @ConfigurableProperty(category = ElementTypeCategory.MOB, comment = "The id of the Werewolf villager.")
    public static int villagerID = Reference.VILLAGER_WEREWOLF;

    /**
     * Make a new instance.
     */
    public WerewolfVillagerConfig() {
        super(
        	villagerID,
            "werewolfVillager",
            null,
            WerewolfVillager.class
        );
    }
    
    @Override
    public boolean isEnabled() {
        return super.isEnabled() && isEnabled;
    }
    
    @Override
    public void save() {
    	// Make sure the configured ID is set before we need it.
    	this.ID = villagerID;
    	super.save();
    }
    
}
