package org.cyclops.evilcraft.loot.functions;

import org.cyclops.cyclopscore.config.extendedconfig.LootFunctionConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class LootFunctionCopyDisplayStandDataConfig extends LootFunctionConfig {
    public LootFunctionCopyDisplayStandDataConfig() {
        super(EvilCraft._instance, "copy_display_stand_data", LootFunctionCopyDisplayStandData.TYPE);
    }
}
