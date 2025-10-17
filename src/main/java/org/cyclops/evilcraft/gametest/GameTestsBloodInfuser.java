package org.cyclops.evilcraft.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import org.cyclops.cyclopscore.gametest.GameTest;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityBloodInfuser;
import org.cyclops.evilcraft.blockentity.BlockEntityDarkTank;

public class GameTestsBloodInfuser {

    public static final String TEMPLATE_EMPTY = Reference.MOD_ID + ":empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 0, 2);

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBloodInfuserDarkPowerGem(GameTestHelper helper) {
        // Add infuser
        helper.setBlock(POS, RegistryEntries.BLOCK_BLOOD_INFUSER.get());
        BlockEntityBloodInfuser infuser = helper.getBlockEntity(POS, BlockEntityBloodInfuser.class);
        infuser.getInventory().setItem(BlockEntityBloodInfuser.SLOT_INFUSE, new ItemStack(RegistryEntries.ITEM_DARK_GEM));
        infuser.getInventory().setItem(BlockEntityBloodInfuser.SLOTS, new ItemStack(RegistryEntries.ITEM_PROMISE_SPEED, 4));

        // Add tank above with blood
        helper.setBlock(POS.above(), RegistryEntries.BLOCK_DARK_TANK.get());
        BlockEntityDarkTank tank = helper.getBlockEntity(POS.above(), BlockEntityDarkTank.class);
        tank.getTank().setFluid(new FluidStack(RegistryEntries.FLUID_BLOOD, 8000));
        tank.setEnabled(true);

        helper.succeedWhen(() -> {
            helper.assertFalse(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).isEmpty(), Component.literal("Result is not available"));
            helper.assertTrue(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).getItem() == RegistryEntries.ITEM_DARK_POWER_GEM.get(), Component.literal("Result item is wrong"));
            helper.assertTrue(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).getCount() == 1, Component.literal("Result item count is wrong"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 200)
    public void testBloodInfuserBowlOfPromisesTier0(GameTestHelper helper) {
        // Add infuser
        helper.setBlock(POS, RegistryEntries.BLOCK_BLOOD_INFUSER.get());
        BlockEntityBloodInfuser infuser = helper.getBlockEntity(POS, BlockEntityBloodInfuser.class);
        infuser.getTank().setFluid(new FluidStack(RegistryEntries.FLUID_BLOOD, 5000));
        infuser.getInventory().setItem(BlockEntityBloodInfuser.SLOT_INFUSE, new ItemStack(RegistryEntries.ITEM_BOWL_OF_PROMISES_DUSTED));
        infuser.getInventory().setItem(BlockEntityBloodInfuser.SLOTS, new ItemStack(RegistryEntries.ITEM_PROMISE_SPEED, 4));

        helper.succeedWhen(() -> {
            helper.assertFalse(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).isEmpty(), Component.literal("Result is not available"));
            helper.assertTrue(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).getItem() == RegistryEntries.ITEM_BOWL_OF_PROMISES_TIER0.get(), Component.literal("Result item is wrong"));
            helper.assertTrue(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).getCount() == 1, Component.literal("Result item count is wrong"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 200)
    public void testBloodInfuserBowlOfPromisesTier1(GameTestHelper helper) {
        // Add infuser
        helper.setBlock(POS, RegistryEntries.BLOCK_BLOOD_INFUSER.get());
        BlockEntityBloodInfuser infuser = helper.getBlockEntity(POS, BlockEntityBloodInfuser.class);
        infuser.getTank().setFluid(new FluidStack(RegistryEntries.FLUID_BLOOD, 5000));
        infuser.getInventory().setItem(BlockEntityBloodInfuser.SLOT_INFUSE, new ItemStack(RegistryEntries.ITEM_BOWL_OF_PROMISES_DUSTED));
        infuser.getInventory().setItem(BlockEntityBloodInfuser.SLOTS, new ItemStack(RegistryEntries.ITEM_PROMISE_TIER_1, 1));
        infuser.getInventory().setItem(BlockEntityBloodInfuser.SLOTS + 1, new ItemStack(RegistryEntries.ITEM_PROMISE_SPEED, 4));

        helper.succeedWhen(() -> {
            helper.assertFalse(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).isEmpty(), Component.literal("Result is not available"));
            helper.assertTrue(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).getItem() == RegistryEntries.ITEM_BOWL_OF_PROMISES_TIER1.get(), Component.literal("Result item is wrong"));
            helper.assertTrue(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).getCount() == 1, Component.literal("Result item count is wrong"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 200)
    public void testBloodInfuserBowlOfPromisesTier2(GameTestHelper helper) {
        // Add infuser
        helper.setBlock(POS, RegistryEntries.BLOCK_BLOOD_INFUSER.get());
        BlockEntityBloodInfuser infuser = helper.getBlockEntity(POS, BlockEntityBloodInfuser.class);
        infuser.getTank().setFluid(new FluidStack(RegistryEntries.FLUID_BLOOD, 5000));
        infuser.getInventory().setItem(BlockEntityBloodInfuser.SLOT_INFUSE, new ItemStack(RegistryEntries.ITEM_BOWL_OF_PROMISES_DUSTED));
        infuser.getInventory().setItem(BlockEntityBloodInfuser.SLOTS, new ItemStack(RegistryEntries.ITEM_PROMISE_TIER_2, 1));
        infuser.getInventory().setItem(BlockEntityBloodInfuser.SLOTS + 1, new ItemStack(RegistryEntries.ITEM_PROMISE_SPEED, 4));

        helper.succeedWhen(() -> {
            helper.assertFalse(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).isEmpty(), Component.literal("Result is not available"));
            helper.assertTrue(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).getItem() == RegistryEntries.ITEM_BOWL_OF_PROMISES_TIER2.get(), Component.literal("Result item is wrong"));
            helper.assertTrue(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).getCount() == 1, Component.literal("Result item count is wrong"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 200)
    public void testBloodInfuserBowlOfPromisesTier3(GameTestHelper helper) {
        // Add infuser
        helper.setBlock(POS, RegistryEntries.BLOCK_BLOOD_INFUSER.get());
        BlockEntityBloodInfuser infuser = helper.getBlockEntity(POS, BlockEntityBloodInfuser.class);
        infuser.getTank().setFluid(new FluidStack(RegistryEntries.FLUID_BLOOD, 5000));
        infuser.getInventory().setItem(BlockEntityBloodInfuser.SLOT_INFUSE, new ItemStack(RegistryEntries.ITEM_BOWL_OF_PROMISES_DUSTED));
        infuser.getInventory().setItem(BlockEntityBloodInfuser.SLOTS, new ItemStack(RegistryEntries.ITEM_PROMISE_TIER_3, 1));
        infuser.getInventory().setItem(BlockEntityBloodInfuser.SLOTS + 1, new ItemStack(RegistryEntries.ITEM_PROMISE_SPEED, 4));

        helper.succeedWhen(() -> {
            helper.assertFalse(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).isEmpty(), Component.literal("Result is not available"));
            helper.assertTrue(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).getItem() == RegistryEntries.ITEM_BOWL_OF_PROMISES_TIER3.get(), Component.literal("Result item is wrong"));
            helper.assertTrue(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).getCount() == 1, Component.literal("Result item count is wrong"));
        });
    }

}
