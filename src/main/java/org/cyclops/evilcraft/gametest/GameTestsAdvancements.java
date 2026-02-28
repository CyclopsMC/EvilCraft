package org.cyclops.evilcraft.gametest;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.cyclops.cyclopscore.RegistryEntriesCommon;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;

import java.util.ArrayList;
import java.util.List;

/**
 * Game tests for all EvilCraft advancements.
 * @author rubensworks
 */
@GameTestHolder(Reference.MOD_ID)
@PrefixGameTestTemplate(false)
public class GameTestsAdvancements {

    public static final String TEMPLATE_EMPTY = "empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 0, 2);

    private static AdvancementHolder getAdvancement(GameTestHelper helper, String id) {
        return helper.getLevel().getServer().getAdvancements().get(ResourceLocation.parse("evilcraft:" + id));
    }

    private static void assertAdvancementDone(GameTestHelper helper, ServerPlayer player, AdvancementHolder advancement) {
        AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
        helper.assertTrue(progress.isDone(), "Advancement " + advancement.id() + " should be done");
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementRoot(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        AdvancementHolder advancement = getAdvancement(helper, "root");
        helper.assertTrue(advancement != null, "Advancement root should exist");

        RegistryEntriesCommon.CRITERION_TRIGGER_MOD_ITEM_OBTAINED.value()
                .trigger(player, instance -> instance.test(player, new ItemStack(RegistryEntries.ITEM_DARK_GEM.get())));

        assertAdvancementDone(helper, player, advancement);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementFirstAge(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        AdvancementHolder advancement = getAdvancement(helper, "first_age");
        helper.assertTrue(advancement != null, "Advancement first_age should exist");

        ItemStack darkGem = new ItemStack(RegistryEntries.ITEM_DARK_GEM.get());
        CriteriaTriggers.INVENTORY_CHANGED.trigger(player, player.getInventory(), darkGem);

        assertAdvancementDone(helper, player, advancement);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementSecondAge(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        AdvancementHolder advancement = getAdvancement(helper, "second_age");
        helper.assertTrue(advancement != null, "Advancement second_age should exist");

        ItemStack bloodExtractor = new ItemStack(RegistryEntries.ITEM_BLOOD_EXTRACTOR.get());
        RegistryEntriesCommon.CRITERION_TRIGGER_ITEM_CRAFTED.value()
                .trigger(player, instance -> instance.test(player, bloodExtractor));

        assertAdvancementDone(helper, player, advancement);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementCannibal(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        AdvancementHolder advancement = getAdvancement(helper, "cannibal");
        helper.assertTrue(advancement != null, "Advancement cannibal should exist");

        ItemStack fleshHumanoid = new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.parse("evilcraft:flesh_humanoid")));
        CriteriaTriggers.CONSUME_ITEM.trigger(player, fleshHumanoid);

        assertAdvancementDone(helper, player, advancement);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementClosure(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        AdvancementHolder advancement = getAdvancement(helper, "closure");
        helper.assertTrue(advancement != null, "Advancement closure should exist");

        var zombie = helper.spawnWithNoFreeWill(EntityType.ZOMBIE, POS.above());
        RegistryEntries.TRIGGER_BOX_OF_ETERNAL_CLOSURE_CAPTURE.get().test(player, zombie);

        assertAdvancementDone(helper, player, advancement);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementEvilSource(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        AdvancementHolder advancement = getAdvancement(helper, "evil_source");
        helper.assertTrue(advancement != null, "Advancement evil_source should exist");

        ItemStack originsOfDarkness = new ItemStack(RegistryEntries.ITEM_ORIGINS_OF_DARKNESS.get());
        CriteriaTriggers.INVENTORY_CHANGED.trigger(player, player.getInventory(), originsOfDarkness);

        assertAdvancementDone(helper, player, advancement);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementFart(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        AdvancementHolder advancement = getAdvancement(helper, "fart");
        helper.assertTrue(advancement != null, "Advancement fart should exist");

        RegistryEntries.TRIGGER_FART.get().test(player);

        assertAdvancementDone(helper, player, advancement);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementMasterDistorter(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        AdvancementHolder advancement = getAdvancement(helper, "master_distorter");
        helper.assertTrue(advancement != null, "Advancement master_distorter should exist");

        List<net.minecraft.world.entity.Entity> entities = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            entities.add(helper.spawnWithNoFreeWill(EntityType.ZOMBIE, POS.above().offset(i, 0, 0)));
        }
        RegistryEntries.TRIGGER_DISTORT.get().test(player, entities);

        assertAdvancementDone(helper, player, advancement);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementPlayerDistorter(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        AdvancementHolder advancement = getAdvancement(helper, "player_distorter");
        helper.assertTrue(advancement != null, "Advancement player_distorter should exist");

        ServerPlayer target = helper.makeMockServerPlayerInLevel();
        RegistryEntries.TRIGGER_DISTORT.get().test(player, List.of(target));

        assertAdvancementDone(helper, player, advancement);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementPlayerDevastator(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        AdvancementHolder advancement = getAdvancement(helper, "player_devastator");
        helper.assertTrue(advancement != null, "Advancement player_devastator should exist");

        ServerPlayer target = helper.makeMockServerPlayerInLevel();
        RegistryEntries.TRIGGER_NECROMANCE_TRIGGER.get().test(player, target);

        assertAdvancementDone(helper, player, advancement);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementPowerCrafting(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        AdvancementHolder advancement = getAdvancement(helper, "power_crafting");
        helper.assertTrue(advancement != null, "Advancement power_crafting should exist");

        ItemStack exaltedCrafter = new ItemStack(RegistryEntries.ITEM_EXALTED_CRAFTER.get());
        RegistryEntriesCommon.CRITERION_TRIGGER_ITEM_CRAFTED.value()
                .trigger(player, instance -> instance.test(player, exaltedCrafter));

        assertAdvancementDone(helper, player, advancement);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementSpiritCooker(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        AdvancementHolder advancement = getAdvancement(helper, "spirit_cooker");
        helper.assertTrue(advancement != null, "Advancement spirit_cooker should exist");

        ItemStack spiritFurnace = new ItemStack(RegistryEntries.BLOCK_SPIRIT_FURNACE.get().asItem());
        RegistryEntriesCommon.CRITERION_TRIGGER_ITEM_CRAFTED.value()
                .trigger(player, instance -> instance.test(player, spiritFurnace));

        assertAdvancementDone(helper, player, advancement);
        helper.succeed();
    }

}
