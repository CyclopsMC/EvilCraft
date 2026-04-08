package org.cyclops.evilcraft.loot.functions;

import org.cyclops.cyclopscore.config.extendedconfig.LootFunctionConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class LootFunctionCopyEntangledChaliceDataConfig extends LootFunctionConfigCommon<IModBase> {
    public LootFunctionCopyEntangledChaliceDataConfig() {
        super(EvilCraft._instance, "copy_entangled_chalice_data", LootFunctionCopyEntangledChaliceData.CODEC);
    }
}
