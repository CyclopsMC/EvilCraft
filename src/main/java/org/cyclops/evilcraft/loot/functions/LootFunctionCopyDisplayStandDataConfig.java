package org.cyclops.evilcraft.loot.functions;

import org.cyclops.cyclopscore.config.extendedconfig.LootFunctionConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class LootFunctionCopyDisplayStandDataConfig extends LootFunctionConfigCommon<IModBase> {
    public LootFunctionCopyDisplayStandDataConfig() {
        super(EvilCraft._instance, "copy_display_stand_data", LootFunctionCopyDisplayStandData.TYPE);
    }
}
