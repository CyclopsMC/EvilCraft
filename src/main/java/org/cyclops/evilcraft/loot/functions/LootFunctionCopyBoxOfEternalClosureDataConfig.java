package org.cyclops.evilcraft.loot.functions;

import org.cyclops.cyclopscore.config.extendedconfig.LootFunctionConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class LootFunctionCopyBoxOfEternalClosureDataConfig extends LootFunctionConfig {
    public LootFunctionCopyBoxOfEternalClosureDataConfig() {
        super(EvilCraft._instance, "copy_box_of_eternal_closure_data", LootFunctionCopyBoxOfEternalClosureData.TYPE);
    }
}
