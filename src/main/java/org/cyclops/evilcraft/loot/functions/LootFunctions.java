package org.cyclops.evilcraft.loot.functions;

import net.minecraft.world.storage.loot.functions.LootFunctionManager;

/**
 * Loot function-related logic.
 * @author rubensworks
 */
public class LootFunctions {

    public static void load() {
        LootFunctionManager.registerFunction(new LootFunctionCopyBoxOfEternalClosureData.Serializer());
        LootFunctionManager.registerFunction(new LootFunctionCopyDisplayStandData.Serializer());
        LootFunctionManager.registerFunction(new LootFunctionCopyEntangledChaliceData.Serializer());
        LootFunctionManager.registerFunction(new LootFunctionCopyTankData.Serializer());
    }

}
