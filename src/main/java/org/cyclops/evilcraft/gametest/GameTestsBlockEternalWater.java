package org.cyclops.evilcraft.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityDarkTank;
import org.cyclops.evilcraft.blockentity.BlockEntityEternalWater;

@GameTestHolder(Reference.MOD_ID)
@PrefixGameTestTemplate(false)
public class GameTestsBlockEternalWater {

    public static final String TEMPLATE_EMPTY = "empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 0, 2);

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 200)
    public void testBlockEternalWaterAutoOutputEnabled(GameTestHelper helper) {
        // Place eternal water block
        helper.setBlock(POS, RegistryEntries.BLOCK_ETERNAL_WATER.get());
        BlockEntityEternalWater eternalWater = helper.getBlockEntity(POS);

        // Place an empty dark tank below to catch water
        helper.setBlock(POS.below(), RegistryEntries.BLOCK_DARK_TANK.get());
        BlockEntityDarkTank tank = helper.getBlockEntity(POS.below());

        helper.succeedWhen(() -> {
            // Check that eternal water block is enabled by default
            helper.assertTrue(eternalWater.isEnabled(), "Eternal water block should be enabled by default");

            // Check that water has been pushed to the tank
            helper.assertFalse(tank.getTank().isEmpty(), "Tank should have water from eternal water block");
            helper.assertTrue(FluidStack.matches(tank.getTank().getFluid(), new FluidStack(Fluids.WATER, tank.getTank().getFluidAmount())), "Tank should contain water");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 100)
    public void testBlockEternalWaterToggleDisable(GameTestHelper helper) {
        // Place eternal water block
        helper.setBlock(POS, RegistryEntries.BLOCK_ETERNAL_WATER.get());
        BlockEntityEternalWater eternalWater = helper.getBlockEntity(POS);

        // Create a player to interact with the block
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setPos(helper.absolutePos(POS).getCenter());

        // Right-click without an item to toggle
        BlockHitResult hitResult = new BlockHitResult(
                helper.absolutePos(POS).getCenter(),
                Direction.UP,
                helper.absolutePos(POS),
                false
        );
        helper.getLevel().getBlockState(helper.absolutePos(POS))
                .useItemOn(ItemStack.EMPTY, helper.getLevel(), player, InteractionHand.MAIN_HAND, hitResult);

        helper.succeedWhen(() -> {
            // Check that eternal water block is now disabled
            helper.assertFalse(eternalWater.isEnabled(), "Eternal water block should be disabled after toggle");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 100)
    public void testBlockEternalWaterToggleEnable(GameTestHelper helper) {
        // Place eternal water block and disable it
        helper.setBlock(POS, RegistryEntries.BLOCK_ETERNAL_WATER.get());
        BlockEntityEternalWater eternalWater = helper.getBlockEntity(POS);
        eternalWater.setEnabled(false);

        // Create a player to interact with the block
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setPos(helper.absolutePos(POS).getCenter());

        // Right-click without an item to toggle
        BlockHitResult hitResult = new BlockHitResult(
                helper.absolutePos(POS).getCenter(),
                Direction.UP,
                helper.absolutePos(POS),
                false
        );
        helper.getLevel().getBlockState(helper.absolutePos(POS))
                .useItemOn(ItemStack.EMPTY, helper.getLevel(), player, InteractionHand.MAIN_HAND, hitResult);

        helper.succeedWhen(() -> {
            // Check that eternal water block is now enabled
            helper.assertTrue(eternalWater.isEnabled(), "Eternal water block should be enabled after toggle");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 200)
    public void testBlockEternalWaterNoOutputWhenDisabled(GameTestHelper helper) {
        // Place eternal water block and disable it
        helper.setBlock(POS, RegistryEntries.BLOCK_ETERNAL_WATER.get());
        BlockEntityEternalWater eternalWater = helper.getBlockEntity(POS);
        eternalWater.setEnabled(false);

        // Place an empty dark tank below
        helper.setBlock(POS.below(), RegistryEntries.BLOCK_DARK_TANK.get());
        BlockEntityDarkTank tank = helper.getBlockEntity(POS.below());

        // Wait for a few ticks to ensure ticker runs
        helper.runAfterDelay(150, () -> {
            // Check that no water has been pushed to the tank
            helper.assertTrue(tank.getTank().isEmpty(), "Tank should be empty when eternal water block is disabled");
        });

        helper.succeedWhen(() -> {
            // Final check that tank is still empty
            helper.assertTrue(tank.getTank().isEmpty(), "Tank should remain empty when eternal water block is disabled");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 300)
    public void testBlockEternalWaterOutputMultipleDirections(GameTestHelper helper) {
        // Place eternal water block
        helper.setBlock(POS, RegistryEntries.BLOCK_ETERNAL_WATER.get());
        BlockEntityEternalWater eternalWater = helper.getBlockEntity(POS);

        // Place empty dark tanks in multiple directions
        helper.setBlock(POS.below(), RegistryEntries.BLOCK_DARK_TANK.get());
        helper.setBlock(POS.above(), RegistryEntries.BLOCK_DARK_TANK.get());
        helper.setBlock(POS.north(), RegistryEntries.BLOCK_DARK_TANK.get());
        helper.setBlock(POS.south(), RegistryEntries.BLOCK_DARK_TANK.get());
        helper.setBlock(POS.east(), RegistryEntries.BLOCK_DARK_TANK.get());
        helper.setBlock(POS.west(), RegistryEntries.BLOCK_DARK_TANK.get());

        BlockEntityDarkTank tankBelow = helper.getBlockEntity(POS.below());
        BlockEntityDarkTank tankAbove = helper.getBlockEntity(POS.above());
        BlockEntityDarkTank tankNorth = helper.getBlockEntity(POS.north());
        BlockEntityDarkTank tankSouth = helper.getBlockEntity(POS.south());
        BlockEntityDarkTank tankEast = helper.getBlockEntity(POS.east());
        BlockEntityDarkTank tankWest = helper.getBlockEntity(POS.west());

        helper.succeedWhen(() -> {
            // Check that eternal water block is enabled
            helper.assertTrue(eternalWater.isEnabled(), "Eternal water block should be enabled");

            // Check that water has been pushed to all tanks
            helper.assertFalse(tankBelow.getTank().isEmpty(), "Tank below should have water");
            helper.assertFalse(tankAbove.getTank().isEmpty(), "Tank above should have water");
            helper.assertFalse(tankNorth.getTank().isEmpty(), "Tank north should have water");
            helper.assertFalse(tankSouth.getTank().isEmpty(), "Tank south should have water");
            helper.assertFalse(tankEast.getTank().isEmpty(), "Tank east should have water");
            helper.assertFalse(tankWest.getTank().isEmpty(), "Tank west should have water");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 100)
    public void testBlockEternalWaterExtractWithBucket(GameTestHelper helper) {
        // Place eternal water block
        helper.setBlock(POS, RegistryEntries.BLOCK_ETERNAL_WATER.get());
        BlockEntityEternalWater eternalWater = helper.getBlockEntity(POS);

        // Create a player with a bucket
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setPos(helper.absolutePos(POS).getCenter());
        ItemStack bucket = new ItemStack(net.minecraft.world.item.Items.BUCKET);
        player.setItemInHand(InteractionHand.MAIN_HAND, bucket);

        // Right-click with bucket
        BlockHitResult hitResult = new BlockHitResult(
                helper.absolutePos(POS).getCenter(),
                Direction.UP,
                helper.absolutePos(POS),
                false
        );
        helper.getLevel().getBlockState(helper.absolutePos(POS))
                .useItemOn(bucket, helper.getLevel(), player, InteractionHand.MAIN_HAND, hitResult);

        helper.succeedWhen(() -> {
            // Check that player now has a water bucket
            ItemStack handStack = player.getItemInHand(InteractionHand.MAIN_HAND);
            helper.assertTrue(
                    handStack.getItem() == net.minecraft.world.item.Items.WATER_BUCKET,
                    "Player should have water bucket after extracting from eternal water block"
            );

            // Check that eternal water block still exists
            helper.assertBlockPresent(RegistryEntries.BLOCK_ETERNAL_WATER.get(), POS);
        });
    }

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 100)
    public void testBlockEternalWaterExtractWhenDisabled(GameTestHelper helper) {
        // Place eternal water block and disable it
        helper.setBlock(POS, RegistryEntries.BLOCK_ETERNAL_WATER.get());
        BlockEntityEternalWater eternalWater = helper.getBlockEntity(POS);
        eternalWater.setEnabled(false);

        // Create a player with a bucket
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setPos(helper.absolutePos(POS).getCenter());
        ItemStack bucket = new ItemStack(net.minecraft.world.item.Items.BUCKET);
        player.setItemInHand(InteractionHand.MAIN_HAND, bucket);

        // Right-click with bucket
        BlockHitResult hitResult = new BlockHitResult(
                helper.absolutePos(POS).getCenter(),
                Direction.UP,
                helper.absolutePos(POS),
                false
        );
        helper.getLevel().getBlockState(helper.absolutePos(POS))
                .useItemOn(bucket, helper.getLevel(), player, InteractionHand.MAIN_HAND, hitResult);

        helper.succeedWhen(() -> {
            // Check that player now has a water bucket
            ItemStack handStack = player.getItemInHand(InteractionHand.MAIN_HAND);
            helper.assertTrue(
                    handStack.getItem() == net.minecraft.world.item.Items.WATER_BUCKET,
                    "Player should be able to extract water even when auto-output is disabled"
            );

            // Check that eternal water block still exists and is still disabled
            helper.assertBlockPresent(RegistryEntries.BLOCK_ETERNAL_WATER.get(), POS);
            helper.assertFalse(eternalWater.isEnabled(), "Eternal water block should remain disabled");
        });
    }

}
