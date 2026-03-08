package org.cyclops.evilcraft.gametest;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.cyclops.cyclopscore.gametest.GameTest;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityBoxOfEternalClosure;
import org.cyclops.evilcraft.entity.effect.EntityNecromancersHead;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpirit;

import java.util.List;

/**
 * Game tests for all EvilCraft advancements.
 * @author rubensworks
 */
public class GameTestsAdvancements {

    public static final String TEMPLATE_EMPTY = Reference.MOD_ID + ":empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 0, 2);

    private static AdvancementHolder getAdvancement(GameTestHelper helper, String id) {
        return helper.getLevel().getServer().getAdvancements().get(Identifier.parse("evilcraft:" + id));
    }

    private static void assertAdvancementDone(GameTestHelper helper, ServerPlayer player, AdvancementHolder advancement) {
        AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
        helper.assertTrue(progress.isDone(), "Advancement " + advancement.id() + " should be done");
    }

    private static void assertAdvancementNotDone(GameTestHelper helper, ServerPlayer player, AdvancementHolder advancement) {
        AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
        if (progress.isDone()) {
            throw new GameTestAssertException(Component.literal("Advancement " + advancement.id() + " should NOT be done"), (int) helper.getTick());
        }
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementRoot(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        AdvancementHolder advancement = getAdvancement(helper, "root");
        helper.assertTrue(advancement != null, "Advancement root should exist");

        // Simulate picking up an EvilCraft item, which fires the mod_item_obtained trigger
        ItemStack darkGemStack = new ItemStack(RegistryEntries.ITEM_DARK_GEM.get());
        net.minecraft.world.entity.item.ItemEntity itemEntity =
                new net.minecraft.world.entity.item.ItemEntity(helper.getLevel(),
                        helper.absolutePos(POS).getX(),
                        helper.absolutePos(POS).getY(),
                        helper.absolutePos(POS).getZ(),
                        darkGemStack);
        NeoForge.EVENT_BUS.post(new ItemEntityPickupEvent.Post(player, itemEntity, darkGemStack.copy()));

        assertAdvancementDone(helper, player, advancement);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementFirstAge(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        AdvancementHolder advancement = getAdvancement(helper, "first_age");
        helper.assertTrue(advancement != null, "Advancement first_age should exist");

        // Add dark_gem to player inventory, which fires the inventory_changed trigger via the container listener
        player.getInventory().add(new ItemStack(RegistryEntries.ITEM_DARK_GEM.get()));
        player.inventoryMenu.broadcastChanges();

        assertAdvancementDone(helper, player, advancement);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementSecondAge(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        AdvancementHolder advancement = getAdvancement(helper, "second_age");
        helper.assertTrue(advancement != null, "Advancement second_age should exist");

        // Simulate crafting the blood_extractor, which fires the item_crafted trigger
        NeoForge.EVENT_BUS.post(new PlayerEvent.ItemCraftedEvent(player,
                new ItemStack(RegistryEntries.ITEM_BLOOD_EXTRACTOR.get()), player.getInventory()));

        assertAdvancementDone(helper, player, advancement);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementCannibal(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        AdvancementHolder advancement = getAdvancement(helper, "cannibal");
        helper.assertTrue(advancement != null, "Advancement cannibal should exist");

        // Consume flesh_humanoid, which triggers consume_item inside ItemWerewolfFlesh.finishUsingItem
        ItemStack fleshHumanoid = new ItemStack(BuiltInRegistries.ITEM.getValue(Identifier.parse("evilcraft:flesh_humanoid")));
        fleshHumanoid.finishUsingItem(helper.getLevel(), player);

        assertAdvancementDone(helper, player, advancement);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementClosure(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        AdvancementHolder advancement = getAdvancement(helper, "closure");
        helper.assertTrue(advancement != null, "Advancement closure should exist");

        // Set up box of eternal closure
        helper.setBlock(POS, RegistryEntries.BLOCK_BOX_OF_ETERNAL_CLOSURE.value());
        BlockEntityBoxOfEternalClosure box = helper.getBlockEntity(POS, BlockEntityBoxOfEternalClosure.class);

        // Create a vengeance spirit with the player as an entangling player, then capture it
        EntityVengeanceSpirit spirit = new EntityVengeanceSpirit(RegistryEntries.ENTITY_VENGEANCE_SPIRIT.get(), helper.getLevel());
        spirit.setInnerEntityType(EntityType.ZOMBIE);
        spirit.addEntanglingPlayer(player);
        box.captureSpirit(spirit);

        assertAdvancementDone(helper, player, advancement);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementEvilSource(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        AdvancementHolder advancement = getAdvancement(helper, "evil_source");
        helper.assertTrue(advancement != null, "Advancement evil_source should exist");

        // Add origins_of_darkness to player inventory, which fires the inventory_changed trigger
        player.getInventory().add(new ItemStack(RegistryEntries.ITEM_ORIGINS_OF_DARKNESS.get()));
        player.inventoryMenu.broadcastChanges();

        assertAdvancementDone(helper, player, advancement);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementFart(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        AdvancementHolder advancement = getAdvancement(helper, "fart");
        helper.assertTrue(advancement != null, "Advancement fart should exist");

        // Process the fart server-side: FartPacket.actionServer fires TRIGGER_FART then broadcasts
        // particle effects to nearby clients. The broadcast fails in game tests (no connected clients),
        // so we only fire the trigger here (the cosmetic broadcast does not affect the advancement).
        RegistryEntries.TRIGGER_FART.get().test(player);

        assertAdvancementDone(helper, player, advancement);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementMasterDistorter(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        player.setPos(helper.absolutePos(POS.above()).getCenter());
        AdvancementHolder advancement = getAdvancement(helper, "master_distorter");
        helper.assertTrue(advancement != null, "Advancement master_distorter should exist");

        // Spawn 10 zombies nearby
        for (int i = 0; i < 10; i++) {
            helper.spawnWithNoFreeWill(EntityType.ZOMBIE, POS.above().offset(i % 4, 0, i / 4));
        }

        // Use the mace of distortion with full charge, which calls distortEntities on all nearby entities
        useMaceOfDistortionFullCharge(helper, player);

        assertAdvancementDone(helper, player, advancement);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementPlayerDistorter(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        player.setPos(helper.absolutePos(POS.above()).getCenter());
        AdvancementHolder advancement = getAdvancement(helper, "player_distorter");
        helper.assertTrue(advancement != null, "Advancement player_distorter should exist");

        // Place a target player nearby
        ServerPlayer target = helper.makeMockServerPlayerInLevel();
        target.setPos(helper.absolutePos(POS.above().north()).getCenter());

        // Use the mace of distortion with full charge, which calls distortEntities on all nearby entities
        useMaceOfDistortionFullCharge(helper, player);

        assertAdvancementDone(helper, player, advancement);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 200)
    public void testAdvancementPlayerDevastator(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        player.setPos(helper.absolutePos(POS.above()).getCenter());
        AdvancementHolder advancement = getAdvancement(helper, "player_devastator");
        helper.assertTrue(advancement != null, "Advancement player_devastator should exist");

        // Place a target player directly in front (north)
        ServerPlayer target = helper.makeMockServerPlayerInLevel();
        target.setPos(helper.absolutePos(POS.above().north()).getCenter());

        // Throw the necromancer's head at the target player
        EntityNecromancersHead head = new EntityNecromancersHead(helper.getLevel(), player.getX(), player.getY(), player.getZ());
        head.setOwner(player);
        head.setMobType(Zombie.class);
        // Aim north (yRot=180), level pitch; shootFromRotation params: pitch, yaw, roll, velocity, inaccuracy
        player.setYRot(180F);
        player.setXRot(0F);
        head.shootFromRotation(player, player.getXRot(), player.getYRot(), -20F, 5F, 0F);
        helper.getLevel().addFreshEntity(head);

        helper.succeedWhen(() -> assertAdvancementDone(helper, player, advancement));
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementPowerCrafting(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        AdvancementHolder advancement = getAdvancement(helper, "power_crafting");
        helper.assertTrue(advancement != null, "Advancement power_crafting should exist");

        // Simulate crafting the exalted_crafter, which fires the item_crafted trigger
        NeoForge.EVENT_BUS.post(new PlayerEvent.ItemCraftedEvent(player,
                new ItemStack(RegistryEntries.ITEM_EXALTED_CRAFTER.get()), player.getInventory()));

        assertAdvancementDone(helper, player, advancement);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementSpiritCooker(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        AdvancementHolder advancement = getAdvancement(helper, "spirit_cooker");
        helper.assertTrue(advancement != null, "Advancement spirit_cooker should exist");

        // Simulate crafting the spirit_furnace, which fires the item_crafted trigger
        NeoForge.EVENT_BUS.post(new PlayerEvent.ItemCraftedEvent(player,
                new ItemStack(RegistryEntries.BLOCK_SPIRIT_FURNACE.get().asItem()), player.getInventory()));

        assertAdvancementDone(helper, player, advancement);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementRootNegative(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        AdvancementHolder advancement = getAdvancement(helper, "root");
        helper.assertTrue(advancement != null, "Advancement root should exist");

        // Simulate picking up a non-EvilCraft item (minecraft:dirt), which should NOT trigger the root advancement
        ItemStack dirtStack = new ItemStack(Items.DIRT);
        net.minecraft.world.entity.item.ItemEntity itemEntity =
                new net.minecraft.world.entity.item.ItemEntity(helper.getLevel(),
                        helper.absolutePos(POS).getX(),
                        helper.absolutePos(POS).getY(),
                        helper.absolutePos(POS).getZ(),
                        dirtStack);
        NeoForge.EVENT_BUS.post(new ItemEntityPickupEvent.Post(player, itemEntity, dirtStack.copy()));

        assertAdvancementNotDone(helper, player, advancement);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementFirstAgeNegative(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        AdvancementHolder advancement = getAdvancement(helper, "first_age");
        helper.assertTrue(advancement != null, "Advancement first_age should exist");

        // Add dirt to player inventory instead of dark_gem, which should NOT trigger the first_age advancement
        player.getInventory().add(new ItemStack(Items.DIRT));
        player.inventoryMenu.broadcastChanges();

        assertAdvancementNotDone(helper, player, advancement);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementSecondAgeNegative(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        AdvancementHolder advancement = getAdvancement(helper, "second_age");
        helper.assertTrue(advancement != null, "Advancement second_age should exist");

        // Simulate crafting dark_gem (not blood_extractor), which should NOT trigger the second_age advancement
        NeoForge.EVENT_BUS.post(new PlayerEvent.ItemCraftedEvent(player,
                new ItemStack(RegistryEntries.ITEM_DARK_GEM.get()), player.getInventory()));

        assertAdvancementNotDone(helper, player, advancement);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementCannibalNegative(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        AdvancementHolder advancement = getAdvancement(helper, "cannibal");
        helper.assertTrue(advancement != null, "Advancement cannibal should exist");

        // Consume flesh_werewolf instead of flesh_humanoid, which should NOT trigger the cannibal advancement
        ItemStack fleshWerewolf = new ItemStack(RegistryEntries.ITEM_FLESH_WEREWOLF.get());
        fleshWerewolf.finishUsingItem(helper.getLevel(), player);

        assertAdvancementNotDone(helper, player, advancement);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementClosureNegative(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        AdvancementHolder advancement = getAdvancement(helper, "closure");
        helper.assertTrue(advancement != null, "Advancement closure should exist");

        // Set up box of eternal closure
        helper.setBlock(POS, RegistryEntries.BLOCK_BOX_OF_ETERNAL_CLOSURE.value());
        BlockEntityBoxOfEternalClosure box = helper.getBlockEntity(POS, BlockEntityBoxOfEternalClosure.class);

        // Capture a spirit that has NO entangling player, so the trigger is never fired
        EntityVengeanceSpirit spirit = new EntityVengeanceSpirit(RegistryEntries.ENTITY_VENGEANCE_SPIRIT.get(), helper.getLevel());
        spirit.setInnerEntityType(EntityType.ZOMBIE);
        box.captureSpirit(spirit);

        assertAdvancementNotDone(helper, player, advancement);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementEvilSourceNegative(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        AdvancementHolder advancement = getAdvancement(helper, "evil_source");
        helper.assertTrue(advancement != null, "Advancement evil_source should exist");

        // Add dark_gem to player inventory instead of origins_of_darkness, which should NOT trigger the evil_source advancement
        player.getInventory().add(new ItemStack(RegistryEntries.ITEM_DARK_GEM.get()));
        player.inventoryMenu.broadcastChanges();

        assertAdvancementNotDone(helper, player, advancement);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementFartNegative(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        AdvancementHolder advancement = getAdvancement(helper, "fart");
        helper.assertTrue(advancement != null, "Advancement fart should exist");

        // Fire inventory_changed (not the fart trigger), which should NOT trigger the fart advancement
        player.getInventory().add(new ItemStack(RegistryEntries.ITEM_DARK_GEM.get()));
        player.inventoryMenu.broadcastChanges();

        assertAdvancementNotDone(helper, player, advancement);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementMasterDistorterNegative(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        AdvancementHolder advancement = getAdvancement(helper, "master_distorter");
        helper.assertTrue(advancement != null, "Advancement master_distorter should exist");

        // Spawn only 9 zombies and fire the distort trigger directly with that list (one fewer than the required 10)
        List<Zombie> zombies = new java.util.ArrayList<>();
        for (int i = 0; i < 9; i++) {
            zombies.add(helper.spawnWithNoFreeWill(EntityType.ZOMBIE, POS.above().offset(i % 4, 0, i / 4)));
        }
        RegistryEntries.TRIGGER_DISTORT.get().test(player, List.copyOf(zombies));

        assertAdvancementNotDone(helper, player, advancement);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementPlayerDistorterNegative(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        AdvancementHolder advancement = getAdvancement(helper, "player_distorter");
        helper.assertTrue(advancement != null, "Advancement player_distorter should exist");

        // Spawn 10 zombies and fire the distort trigger directly with only those zombies (no player entity)
        List<Zombie> zombies = new java.util.ArrayList<>();
        for (int i = 0; i < 10; i++) {
            zombies.add(helper.spawnWithNoFreeWill(EntityType.ZOMBIE, POS.above().offset(i % 4, 0, i / 4)));
        }
        RegistryEntries.TRIGGER_DISTORT.get().test(player, List.copyOf(zombies));

        assertAdvancementNotDone(helper, player, advancement);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementPlayerDevastatorNegative(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        AdvancementHolder advancement = getAdvancement(helper, "player_devastator");
        helper.assertTrue(advancement != null, "Advancement player_devastator should exist");

        // Necromance a zombie (not a player), which should NOT trigger the player_devastator advancement
        Zombie zombie = helper.spawnWithNoFreeWill(EntityType.ZOMBIE, POS.above());
        RegistryEntries.TRIGGER_NECROMANCE_TRIGGER.get().test(player, zombie);

        assertAdvancementNotDone(helper, player, advancement);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementPowerCraftingNegative(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        AdvancementHolder advancement = getAdvancement(helper, "power_crafting");
        helper.assertTrue(advancement != null, "Advancement power_crafting should exist");

        // Simulate crafting dark_gem (not exalted_crafter), which should NOT trigger the power_crafting advancement
        NeoForge.EVENT_BUS.post(new PlayerEvent.ItemCraftedEvent(player,
                new ItemStack(RegistryEntries.ITEM_DARK_GEM.get()), player.getInventory()));

        assertAdvancementNotDone(helper, player, advancement);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testAdvancementSpiritCookerNegative(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        AdvancementHolder advancement = getAdvancement(helper, "spirit_cooker");
        helper.assertTrue(advancement != null, "Advancement spirit_cooker should exist");

        // Simulate crafting blood_extractor (not spirit_furnace), which should NOT trigger the spirit_cooker advancement
        NeoForge.EVENT_BUS.post(new PlayerEvent.ItemCraftedEvent(player,
                new ItemStack(RegistryEntries.ITEM_BLOOD_EXTRACTOR.get()), player.getInventory()));

        assertAdvancementNotDone(helper, player, advancement);
        helper.succeed();
    }

    private static void useMaceOfDistortionFullCharge(GameTestHelper helper, ServerPlayer player) {
        // Fill the mace with blood, then release with itemInUseCount=0 (full charge = maximum area of effect)
        ItemStack maceStack = new ItemStack(RegistryEntries.ITEM_MACE_OF_DISTORTION.get());
        IModHelpersNeoForge.get().getFluidHelpers().getFluidHandlerItemCapacity(ItemAccess.forStack(maceStack)).ifPresent(
                h -> {
                    try (var ctx = Transaction.openRoot()) {
                        h.insert(FluidResource.of(RegistryEntries.FLUID_BLOOD), h.getTankCapacity(0), ctx);
                        ctx.commit();
                    }
                });
        RegistryEntries.ITEM_MACE_OF_DISTORTION.get().releaseUsing(maceStack, helper.getLevel(), player, 0);
    }

}
