package org.cyclops.evilcraft.gametest;

import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import org.cyclops.cyclopscore.gametest.GameTest;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;

public class GameTestsPendant {

    public static final String TEMPLATE_EMPTY = Reference.MOD_ID + ":empty10";

    /**
     * Verifies that having an empty invigorating pendant in a player's inventory does not crash.
     * The crash was caused by DamageIndicatedItemFluidContainer.canDrain calling extract on an empty
     * fluid resource (minecraft:empty), which throws IllegalArgumentException in NeoForge 21.11.38-beta+.
     */
    @GameTest(template = TEMPLATE_EMPTY)
    public void testInvigoratingPendantInventoryTickEmptyNoCrash(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack pendantStack = new ItemStack(RegistryEntries.ITEM_INVIGORATING_PENDANT);

        // Directly invoke clearBadEffects on an empty pendant - should not throw
        RegistryEntries.ITEM_INVIGORATING_PENDANT.get().clearBadEffects(pendantStack, player);

        helper.succeed();
    }

    /**
     * Verifies that having an empty primed pendant in a player's inventory does not crash.
     */
    @GameTest(template = TEMPLATE_EMPTY)
    public void testPrimedPendantInventoryTickEmptyNoCrash(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack pendantStack = new ItemStack(RegistryEntries.ITEM_PRIMED_PENDANT);

        // canConsume on empty pendant should return false without throwing
        boolean canConsume = RegistryEntries.ITEM_PRIMED_PENDANT.get().canConsume(1, pendantStack, player);
        helper.assertFalse(canConsume, Component.literal("Empty pendant should not be consumable"));

        helper.succeed();
    }

}
