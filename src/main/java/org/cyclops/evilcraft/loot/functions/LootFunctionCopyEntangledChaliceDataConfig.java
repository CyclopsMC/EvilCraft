package org.cyclops.evilcraft.loot.functions;

import org.cyclops.cyclopscore.config.extendedconfig.LootFunctionConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class LootFunctionCopyEntangledChaliceDataConfig extends LootFunctionConfig {
    public LootFunctionCopyEntangledChaliceDataConfig() {
        super(EvilCraft._instance, "copy_entangled_chalice_data", LootFunctionCopyEntangledChaliceData.TYPE);
    }
}
