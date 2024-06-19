package org.cyclops.evilcraft.loot.functions;

import org.cyclops.cyclopscore.config.extendedconfig.LootFunctionConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class LootFunctionCopyTankDataConfig extends LootFunctionConfig {
    public LootFunctionCopyTankDataConfig() {
        super(EvilCraft._instance, "copy_tank_data", LootFunctionCopyTankData.TYPE);
    }
}
