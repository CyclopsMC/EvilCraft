package org.cyclops.evilcraft.entity.villager;

import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.VillagerConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

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
    @ConfigurableProperty(category = ConfigurableTypeCategory.MOB, comment = "Should the Werewolf villager be enabled?", requiresMcRestart = true)
    public static boolean isEnabled = true;
    
    /**
     * The id of the Werewolf villager
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MOB, comment = "The id of the Werewolf villager.", requiresWorldRestart = true, requiresMcRestart = true)
    public static int villagerID = Reference.VILLAGER_WEREWOLF;

    /**
     * Make a new instance.
     */
    public WerewolfVillagerConfig() {
        super(
                EvilCraft._instance,
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
    	this.setId(villagerID);
    	super.save();
    }
    
}
