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
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import org.cyclops.cyclopscore.gametest.GameTest;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockBoxOfEternalClosure;
import org.cyclops.evilcraft.blockentity.BlockEntityBoxOfEternalClosure;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpirit;

import java.util.List;
import java.util.UUID;

public class GameTestsVengeanceSpirits {

    public static final String TEMPLATE_EMPTY = Reference.MOD_ID + ":empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 0, 2);

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 300, environment = "evilcraft:vengeance_spirit_catch")
    public void testVengeanceSpiritCatch(GameTestHelper helper) {
        // Spawn spirit, and pre-freeze it so the box can reliably find and capture it (the box only targets frozen spirits)
        EntityVengeanceSpirit spirit = helper.spawnWithNoFreeWill(RegistryEntries.ENTITY_VENGEANCE_SPIRIT.get(), POS.south().south().above());
        spirit.setInnerEntityType(EntityType.ZOMBIE);
        spirit.setFrozenDuration(200);

        // Let player use vengeance focus
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setPos(helper.absolutePos(POS.north()).getBottomCenter());
        player.setXRot(25F);
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

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 300, environment = "evilcraft:vengeance_spirit_player_catch")
    public void testVengeanceSpiritPlayerCatch(GameTestHelper helper) {
        // Spawn spirit
        EntityVengeanceSpirit spirit = helper.spawnWithNoFreeWill(RegistryEntries.ENTITY_VENGEANCE_SPIRIT.get(), POS.south().south());
        spirit.setPlayerId("068d4de0-3a75-4c6a-9f01-8c37e16a394c");
        spirit.setPlayerName("kroeserr");
        spirit.setInnerEntityType(EntityType.ZOMBIE);

        // Let player use vengeance focus
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setPos(helper.absolutePos(POS.north()).getBottomCenter());
        player.setXRot(25F);
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

    @GameTest(template = TEMPLATE_EMPTY, environment = "evilcraft:vengeance_spirit_release")
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

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 200, environment = "evilcraft:vengeance_spirit_attack")
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
        player.setPos(helper.absolutePos(POS).getBottomCenter());
        player.setXRot(25F);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(RegistryEntries.ITEM_PIERCING_VENGEANCE_FOCUS));
        player.getItemInHand(InteractionHand.MAIN_HAND).use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        helper.onEachTick(() -> player.getItemInHand(InteractionHand.MAIN_HAND).onUseTick(helper.getLevel(), player, 0));

        helper.succeedWhen(() -> {
            helper.assertItemEntityPresent(RegistryEntries.ITEM_VENGEANCE_ESSENCE.get());
            helper.assertEntityNotPresent(RegistryEntries.ENTITY_VENGEANCE_SPIRIT.get());
        });
    }

    @GameTest(template = TEMPLATE_EMPTY, environment = "evilcraft:vengeance_spirit_spawn")
    public void testVengeanceSpiritSpawn(GameTestHelper helper) {
        // Spawn zombie
        Zombie zombie = helper.spawnWithNoFreeWill(EntityType.ZOMBIE, POS.above().south());
        zombie.setHealth(1);

        // Kill zombie with a player-attack damage source while holding the vengeance ring,
        // so shouldDirectSpiritToPlayer returns true and the spawned spirit targets the player.
        // The ring is removed immediately after the kill to prevent inventoryTick from calling
        // toggleVengeanceArea(enableVengeance=false) which would otherwise clear the spirit's target.
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setPos(helper.absolutePos(POS).getBottomCenter());
        player.getInventory().setItem(0, new ItemStack(RegistryEntries.ITEM_VENGEANCE_RING));
        zombie.hurt(helper.getLevel().damageSources().playerAttack(player), 100f);
        player.getInventory().setItem(0, ItemStack.EMPTY);

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

    @GameTest(template = TEMPLATE_EMPTY, environment = "evilcraft:vengeance_spirit_spawn_without_ring")
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
        helper.setBlock(POS.south(), Blocks.STONE);

        // Roof the zombie, so it can't catch fire in the sun and die by a non-player damage source,
        // as that would not spawn a spirit at all.
        // The structure is encased in barriers, but those do not dampen skylight.
        helper.setBlock(POS.above().south().above().above(), Blocks.STONE);

        helper.succeedWhen(() -> {
            helper.assertEntityNotPresent(EntityType.ZOMBIE);
            helper.assertEntityPresent(RegistryEntries.ENTITY_VENGEANCE_SPIRIT.get());
            EntityVengeanceSpirit spirit = helper.getEntities(RegistryEntries.ENTITY_VENGEANCE_SPIRIT.get()).get(0);
            helper.assertTrue(spirit.getTarget() == null, Component.literal("Spirit targets nothing"));
            helper.assertValueEqual(spirit.getInnerEntityType(), EntityType.ZOMBIE, Component.literal("Spirit contains invalid entity type"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY, environment = "evilcraft:vengeance_spirit_spawn_not_when_killed_by_non_player")
    public void testVengeanceSpiritSpawnNotWhenKilledByNonPlayer(GameTestHelper helper) {
        // Spawn zombie
        Zombie zombie = helper.spawnWithNoFreeWill(EntityType.ZOMBIE, POS.above().south());
        zombie.setHealth(1);

        // Kill zombie (use hurt so health drops to 0 immediately, preventing isAlive() returning true for ~20 ticks)
        zombie.hurt(helper.getLevel().damageSources().cactus(), 100f);

        helper.succeedWhen(() -> {
            helper.assertEntityNotPresent(EntityType.ZOMBIE);
            helper.assertEntityNotPresent(RegistryEntries.ENTITY_VENGEANCE_SPIRIT.get());
        });
    }

    @GameTest(template = TEMPLATE_EMPTY, environment = "evilcraft:vengeance_spirit_release_player_drops_empty_box")
    public void testVengeanceSpiritReleasePlayerDropsEmptyBox(GameTestHelper helper) {
        helper.setBlock(POS, Blocks.STONE);

        // Place a box containing a player, as found in loot
        ItemStack boxFilled = new ItemStack(RegistryEntries.ITEM_BOX_OF_ETERNAL_CLOSURE);
        BlockBoxOfEternalClosure.setPlayerContent(boxFilled, UUID.fromString("068d4de0-3a75-4c6a-9f01-8c37e16a394c"), "kroeserr");
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, boxFilled);
        BlockPos posFloor = helper.absolutePos(POS);
        boxFilled.useOn(new UseOnContext(player, InteractionHand.MAIN_HAND, new BlockHitResult(posFloor.getCenter().add(0, 0.5, 0), Direction.UP, posFloor, false)));
        BlockEntityBoxOfEternalClosure box = helper.getBlockEntity(POS.above(), BlockEntityBoxOfEternalClosure.class);
        helper.assertTrue(box.hasSpirit(), "Placed box is empty");
        helper.assertValueEqual(box.getPlayerName(), "kroeserr", "Placed box has invalid player");

        // Open box
        helper.getBlockState(POS.above()).useWithoutItem(helper.getLevel(), player, new BlockHitResult(helper.absolutePos(POS.above()).getCenter(), Direction.DOWN, helper.absolutePos(POS.above()), false));

        helper.succeedWhen(() -> {
            helper.assertFalse(box.hasSpirit(), "Box is not empty");
            helper.assertEntityPresent(RegistryEntries.ENTITY_VENGEANCE_SPIRIT.get());

            // Breaking the box must drop an empty box
            List<ItemStack> drops = Block.getDrops(helper.getBlockState(POS.above()), helper.getLevel(), helper.absolutePos(POS.above()), box);
            helper.assertValueEqual(drops.size(), 1, "Invalid number of drops");
            helper.assertTrue(ItemStack.isSameItemSameComponents(drops.get(0), new ItemStack(RegistryEntries.ITEM_BOX_OF_ETERNAL_CLOSURE)), "Dropped box is not empty: " + drops.get(0).getComponentsPatch());
        });
    }
}
