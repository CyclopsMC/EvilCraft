package org.cyclops.evilcraft.entity.villager;

import org.cyclops.cyclopscore.config.extendedconfig.VillagerConfig;
import org.cyclops.evilcraft.EvilCraft;

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
     * Make a new instance.
     */
    public WerewolfVillagerConfig() {
        super(
            EvilCraft._instance,
        	true,
            "werewolf_villager",
            null,
            WerewolfVillager.class
        );
    }
    
}
