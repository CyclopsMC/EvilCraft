package org.cyclops.evilcraft.loot.functions;

import org.cyclops.cyclopscore.config.extendedconfig.LootFunctionConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class LootFunctionCopyTankDataConfig extends LootFunctionConfigCommon<IModBase> {
    public LootFunctionCopyTankDataConfig() {
        super(EvilCraft._instance, "copy_tank_data", LootFunctionCopyTankData.CODEC);
    }
}
