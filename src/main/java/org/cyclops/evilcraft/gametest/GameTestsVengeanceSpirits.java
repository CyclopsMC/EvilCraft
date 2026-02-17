package org.cyclops.evilcraft.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import org.cyclops.cyclopscore.gametest.GameTest;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityBoxOfEternalClosure;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpirit;


public class GameTestsVengeanceSpirits {

    public static final String TEMPLATE_EMPTY = Reference.MOD_ID + ":empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 0, 2);

    @GameTest(template = TEMPLATE_EMPTY)
    public void testVengeanceSpiritCatch(GameTestHelper helper) {
        // Spawn spirit
        EntityVengeanceSpirit spirit = helper.spawnWithNoFreeWill(RegistryEntries.ENTITY_VENGEANCE_SPIRIT.get(), POS.south().south());
        spirit.setInnerEntityType(EntityType.ZOMBIE);

        // Let player use vengeance focus
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setPos(helper.absolutePos(POS).getBottomCenter());
        player.setXRot(-25F);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(RegistryEntries.ITEM_VENGEANCE_FOCUS));
        player.getItemInHand(InteractionHand.MAIN_HAND).use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        helper.onEachTick(() -> player.getItemInHand(InteractionHand.MAIN_HAND).onUseTick(helper.getLevel(), player, 0));

        // Add box
        helper.setBlock(POS.north(), RegistryEntries.BLOCK_BOX_OF_ETERNAL_CLOSURE.value());
        BlockEntityBoxOfEternalClosure box = helper.getBlockEntity(POS.north(), BlockEntityBoxOfEternalClosure.class);

        helper.succeedWhen(() -> {
            helper.assertTrue(box.hasSpirit(), Component.literal("Box is empty"));
            helper.assertValueEqual(box.getSpiritData().getInnerEntityType(), EntityType.ZOMBIE, Component.literal("Box contains invalid entity type"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testVengeanceSpiritPlayerCatch(GameTestHelper helper) {
        // Spawn spirit
        EntityVengeanceSpirit spirit = helper.spawnWithNoFreeWill(RegistryEntries.ENTITY_VENGEANCE_SPIRIT.get(), POS.south().south());
        spirit.setPlayerId("068d4de0-3a75-4c6a-9f01-8c37e16a394c");
        spirit.setPlayerName("kroeserr");
        spirit.setInnerEntityType(EntityType.ZOMBIE);

        // Let player use vengeance focus
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setPos(helper.absolutePos(POS).getBottomCenter());
        player.setXRot(-25F);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(RegistryEntries.ITEM_VENGEANCE_FOCUS));
        player.getItemInHand(InteractionHand.MAIN_HAND).use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        helper.onEachTick(() -> player.getItemInHand(InteractionHand.MAIN_HAND).onUseTick(helper.getLevel(), player, 0));

        // Add box
        helper.setBlock(POS.north(), RegistryEntries.BLOCK_BOX_OF_ETERNAL_CLOSURE.value());
        BlockEntityBoxOfEternalClosure box = helper.getBlockEntity(POS.north(), BlockEntityBoxOfEternalClosure.class);

        helper.succeedWhen(() -> {
            helper.assertTrue(box.hasSpirit(), Component.literal("Box is empty"));
            helper.assertValueEqual("068d4de0-3a75-4c6a-9f01-8c37e16a394c", box.getPlayerId(), Component.literal("Box player id"));
            helper.assertValueEqual("kroeserr", box.getPlayerName(), Component.literal("Box player name"));
            helper.assertValueEqual(box.getSpiritData().getInnerEntityType(), EntityType.ZOMBIE, Component.literal("Box contains invalid entity type"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testVengeanceSpiritRelease(GameTestHelper helper) {
        // Add filled box
        helper.setBlock(POS.above(), RegistryEntries.BLOCK_BOX_OF_ETERNAL_CLOSURE.value());
        BlockEntityBoxOfEternalClosure box = helper.getBlockEntity(POS.above(), BlockEntityBoxOfEternalClosure.class);
        EntityVengeanceSpirit spiritDummy = new EntityVengeanceSpirit(RegistryEntries.ENTITY_VENGEANCE_SPIRIT.get(), helper.getLevel());
        spiritDummy.setInnerEntityType(EntityType.ZOMBIE);
        box.captureSpirit(spiritDummy);
        box.closeImmediately();

        // Open box
        helper.getBlockState(POS.above()).useWithoutItem(helper.getLevel(), helper.makeMockPlayer(GameType.SURVIVAL), new BlockHitResult(helper.absolutePos(POS.above()).getCenter(), Direction.DOWN, helper.absolutePos(POS.above()), false));

        helper.succeedWhen(() -> {
            helper.assertFalse(box.hasSpirit(), Component.literal("Box is not empty"));
            helper.assertEntityPresent(RegistryEntries.ENTITY_VENGEANCE_SPIRIT.get());
            EntityVengeanceSpirit spirit = helper.getEntities(RegistryEntries.ENTITY_VENGEANCE_SPIRIT.get()).get(0);
            helper.assertValueEqual(spirit.getInnerEntityType(), EntityType.ZOMBIE, Component.literal("Spirit contains invalid entity type"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 200)
    public void testVengeanceSpiritAttack(GameTestHelper helper) {
        // Spawn spirit
        EntityVengeanceSpirit spirit = helper.spawnWithNoFreeWill(RegistryEntries.ENTITY_VENGEANCE_SPIRIT.get(), POS.above().south().south());
        spirit.setInnerEntityType(EntityType.ZOMBIE);

        // Make wall before spirit so it can't move
        helper.setBlock(POS.above().south().south().south(), Blocks.STONE);
        helper.setBlock(POS.above().south().south().south().above(), Blocks.STONE);
        helper.setBlock(POS.above().south().south().above().above(), Blocks.STONE);
        helper.setBlock(POS.south().south(), Blocks.STONE);

        // Let player use piercing vengeance focus
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setPos(helper.absolutePos(POS.above().above()).getBottomCenter());
        player.setXRot(1F);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(RegistryEntries.ITEM_PIERCING_VENGEANCE_FOCUS));
        player.getItemInHand(InteractionHand.MAIN_HAND).use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        helper.onEachTick(() -> player.getItemInHand(InteractionHand.MAIN_HAND).onUseTick(helper.getLevel(), player, 0));

        helper.succeedWhen(() -> {
            helper.assertItemEntityPresent(RegistryEntries.ITEM_VENGEANCE_ESSENCE.get());
            helper.assertEntityNotPresent(RegistryEntries.ENTITY_VENGEANCE_SPIRIT.get());
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testVengeanceSpiritSpawn(GameTestHelper helper) {
        // Spawn zombie
        Zombie zombie = helper.spawnWithNoFreeWill(EntityType.ZOMBIE, POS.above().south());
        zombie.setHealth(1);

        // Let player kill zombie
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setPos(helper.absolutePos(POS).getBottomCenter());
        player.setXRot(1F);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.DIAMOND_SWORD));
        player.getInventory().setItem(0, new ItemStack(RegistryEntries.ITEM_VENGEANCE_RING));
        helper.onEachTick(() -> player.attack(zombie));

        // Make wall before spirit so it can't move
        helper.setBlock(POS.above().south().south().south(), Blocks.STONE);
        helper.setBlock(POS.above().south().south().south().above(), Blocks.STONE);
        helper.setBlock(POS.above().south().south().above().above(), Blocks.STONE);
        helper.setBlock(POS.south().south(), Blocks.STONE);

        helper.succeedWhen(() -> {
            helper.assertEntityNotPresent(EntityType.ZOMBIE);
            helper.assertEntityPresent(RegistryEntries.ENTITY_VENGEANCE_SPIRIT.get());
            EntityVengeanceSpirit spirit = helper.getEntities(RegistryEntries.ENTITY_VENGEANCE_SPIRIT.get()).get(0);
            helper.assertTrue(spirit.getTarget() != null, "Spirit target is null");
            helper.assertValueEqual(spirit.getTarget(), player, "Spirit targets player");
            helper.assertValueEqual(spirit.getInnerEntityType(), EntityType.ZOMBIE, "Spirit contains invalid entity type");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testVengeanceSpiritSpawnWithoutRing(GameTestHelper helper) {
        // Spawn zombie
        Zombie zombie = helper.spawnWithNoFreeWill(EntityType.ZOMBIE, POS.above().south());
        zombie.setHealth(1);

        // Let player kill zombie
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setPos(helper.absolutePos(POS).getBottomCenter());
        player.setXRot(1F);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.DIAMOND_SWORD));
        helper.onEachTick(() -> player.attack(zombie));

        // Make wall before spirit so it can't move
        helper.setBlock(POS.above().south().south().south(), Blocks.STONE);
        helper.setBlock(POS.above().south().south().south().above(), Blocks.STONE);
        helper.setBlock(POS.above().south().south().above().above(), Blocks.STONE);
        helper.setBlock(POS.south().south(), Blocks.STONE);

        helper.succeedWhen(() -> {
            helper.assertEntityNotPresent(EntityType.ZOMBIE);
            helper.assertEntityPresent(RegistryEntries.ENTITY_VENGEANCE_SPIRIT.get());
            EntityVengeanceSpirit spirit = helper.getEntities(RegistryEntries.ENTITY_VENGEANCE_SPIRIT.get()).get(0);
            helper.assertTrue(spirit.getTarget() == null, Component.literal("Spirit targets nothing"));
            helper.assertValueEqual(spirit.getInnerEntityType(), EntityType.ZOMBIE, Component.literal("Spirit contains invalid entity type"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testVengeanceSpiritSpawnNotWhenKilledByNonPlayer(GameTestHelper helper) {
        // Spawn zombie
        Zombie zombie = helper.spawnWithNoFreeWill(EntityType.ZOMBIE, POS.above().south());
        zombie.setHealth(1);

        // Kill zombie
        zombie.die(helper.getLevel().damageSources().cactus());

        helper.succeedWhen(() -> {
            helper.assertEntityNotPresent(EntityType.ZOMBIE);
            helper.assertEntityNotPresent(RegistryEntries.ENTITY_VENGEANCE_SPIRIT.get());
        });
    }

}
