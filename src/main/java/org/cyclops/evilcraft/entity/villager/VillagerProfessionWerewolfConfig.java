package org.cyclops.evilcraft.entity.villager;

import org.cyclops.cyclopscore.config.extendedconfig.VillagerConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link VillagerProfessionWerewolf}.
 * @author rubensworks
 */
public class VillagerProfessionWerewolfConfig extends VillagerConfig {

    public VillagerProfessionWerewolfConfig() {
        super(
                EvilCraft._instance,
                "werewolf",
                VillagerProfessionWerewolf::new
        );
    }

}
