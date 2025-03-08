package org.cyclops.evilcraft.loot.functions;

import org.cyclops.cyclopscore.config.extendedconfig.LootFunctionConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class LootFunctionCopyBoxOfEternalClosureDataConfig extends LootFunctionConfigCommon<IModBase> {
    public LootFunctionCopyBoxOfEternalClosureDataConfig() {
        super(EvilCraft._instance, "copy_box_of_eternal_closure_data", LootFunctionCopyBoxOfEternalClosureData.TYPE);
    }
}
