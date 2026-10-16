package org.cyclops.evilcraft.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.npc.villager.Villager;
import org.cyclops.cyclopscore.gametest.GameTest;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.entity.monster.EntityWerewolf;

public class GameTestsWerewolfVillager {

    public static final String TEMPLATE_EMPTY = Reference.MOD_ID + ":empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 0, 2);

    // Daytime, to avoid werewolf transformation
    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 1200, environment = "evilcraft:werewolf_villager_day")
    public void testWerewolfVillagerFromPoi(GameTestHelper helper) {
        helper.setBlock(POS, RegistryEntries.BLOCK_PURIFIER.value());
        Villager villager = helper.spawn(EntityTypes.VILLAGER, POS.east().east());

        helper.succeedWhen(() -> {
            helper.assertValueEqual(villager.getVillagerData().profession().value(), RegistryEntries.VILLAGER_PROFESSION_WEREWOLF.get(), Component.literal("Villager has no werewolf profession"));
            helper.assertValueEqual(villager.getVillagerData().level(), 2, Component.literal("Werewolf villager has invalid level"));
            helper.assertFalse(villager.getOffers().isEmpty(), Component.literal("Werewolf villager has no trades"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 100, environment = "evilcraft:werewolf_villager_night")
    public void testWerewolfVillagerTransformAtFullMoon(GameTestHelper helper) {
        Villager villager = helper.spawn(EntityTypes.VILLAGER, POS);
        EntityWerewolf.initializeWerewolfVillagerData(villager);

        helper.succeedWhen(() -> helper.assertEntityPresent(RegistryEntries.ENTITY_WEREWOLF.get()));
    }

}
