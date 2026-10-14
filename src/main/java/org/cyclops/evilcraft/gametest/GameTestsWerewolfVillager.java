package org.cyclops.evilcraft.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;

@GameTestHolder(Reference.MOD_ID)
@PrefixGameTestTemplate(false)
public class GameTestsWerewolfVillager {

    public static final String TEMPLATE_EMPTY = "empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 0, 2);

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 1200, batch = "werewolf_villager_day")
    public void testWerewolfVillagerFromPoi(GameTestHelper helper) {
        // Daytime, to avoid werewolf transformation
        helper.getLevel().setDayTime(6000);

        helper.setBlock(POS, RegistryEntries.BLOCK_PURIFIER.value());
        Villager villager = helper.spawn(EntityType.VILLAGER, POS.east().east());

        helper.succeedWhen(() -> {
            helper.assertValueEqual(villager.getVillagerData().getProfession(), RegistryEntries.VILLAGER_PROFESSION_WEREWOLF.get(), "Villager has no werewolf profession");
            helper.assertValueEqual(villager.getVillagerData().getLevel(), 2, "Werewolf villager has invalid level");
            helper.assertFalse(villager.getOffers().isEmpty(), "Werewolf villager has no trades");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 100, batch = "werewolf_villager_night")
    public void testWerewolfVillagerTransformAtFullMoon(GameTestHelper helper) {
        helper.getLevel().setDayTime(18000);

        Villager villager = helper.spawn(EntityType.VILLAGER, POS);
        villager.setVillagerData(villager.getVillagerData().setLevel(2).setProfession(RegistryEntries.VILLAGER_PROFESSION_WEREWOLF.get()));

        helper.succeedWhen(() -> helper.assertEntityPresent(RegistryEntries.ENTITY_WEREWOLF.get()));
    }

}
