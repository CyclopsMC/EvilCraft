package org.cyclops.evilcraft.gametest;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityBoxOfEternalClosure;
import org.cyclops.evilcraft.entity.effect.EntityNecromancersHead;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpirit;

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
        ItemStack fleshHumanoid = new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.parse("evilcraft:flesh_humanoid")));
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
        BlockEntityBoxOfEternalClosure box = helper.getBlockEntity(POS);

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
        EntityNecromancersHead head = new EntityNecromancersHead(helper.getLevel(), player);
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

    private static void useMaceOfDistortionFullCharge(GameTestHelper helper, ServerPlayer player) {
        // Fill the mace with blood, then release with itemInUseCount=0 (full charge = maximum area of effect)
        ItemStack maceStack = new ItemStack(RegistryEntries.ITEM_MACE_OF_DISTORTION.get());
        FluidHelpers.getFluidHandlerItemCapacity(maceStack).ifPresent(
                h -> h.fill(new FluidStack(RegistryEntries.FLUID_BLOOD, h.getCapacity()), IFluidHandler.FluidAction.EXECUTE));
        RegistryEntries.ITEM_MACE_OF_DISTORTION.get().releaseUsing(maceStack, helper.getLevel(), player, 0);
    }

}
