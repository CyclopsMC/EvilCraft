package org.cyclops.evilcraft.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
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
        BlockEntityBloodInfuser blockEntity = getInfuser(helper);
        blockEntity.getInventory().setItem(BlockEntityBloodInfuser.SLOT_INFUSE, new ItemStack(RegistryEntries.ITEM_DARK_GEM));
        blockEntity.getInventory().setItem(BlockEntityBloodInfuser.SLOTS, new ItemStack(RegistryEntries.ITEM_PROMISE_SPEED, 4));

        // Add tank above with blood
        helper.setBlock(POS.above(), RegistryEntries.BLOCK_DARK_TANK.get());
        BlockEntityDarkTank tank = helper.getBlockEntity(POS.above(), BlockEntityDarkTank.class);
        tank.getTank().setFluid(new FluidStack(RegistryEntries.FLUID_BLOOD, 8000));
        tank.setEnabled(true);

        helper.succeedWhen(() -> {
            BlockEntityBloodInfuser infuser = getInfuser(helper);
            helper.assertFalse(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).isEmpty(), Component.literal("Result is not available"));
            helper.assertTrue(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).getItem() == RegistryEntries.ITEM_DARK_POWER_GEM.get(), Component.literal("Result item is wrong"));
            helper.assertTrue(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).getCount() == 1, Component.literal("Result item count is wrong"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 200)
    public void testBloodInfuserBowlOfPromisesTier0(GameTestHelper helper) {
        // Add infuser
        helper.setBlock(POS, RegistryEntries.BLOCK_BLOOD_INFUSER.get());
        BlockEntityBloodInfuser blockEntity = getInfuser(helper);
        blockEntity.getTank().setFluid(new FluidStack(RegistryEntries.FLUID_BLOOD, 5000));
        blockEntity.getInventory().setItem(BlockEntityBloodInfuser.SLOT_INFUSE, new ItemStack(RegistryEntries.ITEM_BOWL_OF_PROMISES_DUSTED));
        blockEntity.getInventory().setItem(BlockEntityBloodInfuser.SLOTS, new ItemStack(RegistryEntries.ITEM_PROMISE_SPEED, 4));

        helper.succeedWhen(() -> {
            BlockEntityBloodInfuser infuser = getInfuser(helper);
            helper.assertFalse(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).isEmpty(), Component.literal("Result is not available"));
            helper.assertTrue(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).getItem() == RegistryEntries.ITEM_BOWL_OF_PROMISES_TIER0.get(), Component.literal("Result item is wrong"));
            helper.assertTrue(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).getCount() == 1, Component.literal("Result item count is wrong"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 200)
    public void testBloodInfuserBowlOfPromisesTier1(GameTestHelper helper) {
        // Add infuser
        helper.setBlock(POS, RegistryEntries.BLOCK_BLOOD_INFUSER.get());
        BlockEntityBloodInfuser blockEntity = getInfuser(helper);
        blockEntity.getTank().setFluid(new FluidStack(RegistryEntries.FLUID_BLOOD, 5000));
        blockEntity.getInventory().setItem(BlockEntityBloodInfuser.SLOT_INFUSE, new ItemStack(RegistryEntries.ITEM_BOWL_OF_PROMISES_DUSTED));
        blockEntity.getInventory().setItem(BlockEntityBloodInfuser.SLOTS, new ItemStack(RegistryEntries.ITEM_PROMISE_TIER_1, 1));
        blockEntity.getInventory().setItem(BlockEntityBloodInfuser.SLOTS + 1, new ItemStack(RegistryEntries.ITEM_PROMISE_SPEED, 4));

        helper.succeedWhen(() -> {
            BlockEntityBloodInfuser infuser = getInfuser(helper);
            helper.assertFalse(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).isEmpty(), Component.literal("Result is not available"));
            helper.assertTrue(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).getItem() == RegistryEntries.ITEM_BOWL_OF_PROMISES_TIER1.get(), Component.literal("Result item is wrong"));
            helper.assertTrue(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).getCount() == 1, Component.literal("Result item count is wrong"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 200)
    public void testBloodInfuserBowlOfPromisesTier2(GameTestHelper helper) {
        // Add infuser
        helper.setBlock(POS, RegistryEntries.BLOCK_BLOOD_INFUSER.get());
        BlockEntityBloodInfuser blockEntity = getInfuser(helper);
        blockEntity.getTank().setFluid(new FluidStack(RegistryEntries.FLUID_BLOOD, 5000));
        blockEntity.getInventory().setItem(BlockEntityBloodInfuser.SLOT_INFUSE, new ItemStack(RegistryEntries.ITEM_BOWL_OF_PROMISES_DUSTED));
        blockEntity.getInventory().setItem(BlockEntityBloodInfuser.SLOTS, new ItemStack(RegistryEntries.ITEM_PROMISE_TIER_2, 1));
        blockEntity.getInventory().setItem(BlockEntityBloodInfuser.SLOTS + 1, new ItemStack(RegistryEntries.ITEM_PROMISE_SPEED, 4));

        helper.succeedWhen(() -> {
            BlockEntityBloodInfuser infuser = getInfuser(helper);
            helper.assertFalse(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).isEmpty(), Component.literal("Result is not available"));
            helper.assertTrue(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).getItem() == RegistryEntries.ITEM_BOWL_OF_PROMISES_TIER2.get(), Component.literal("Result item is wrong"));
            helper.assertTrue(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).getCount() == 1, Component.literal("Result item count is wrong"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 200)
    public void testBloodInfuserBowlOfPromisesTier3(GameTestHelper helper) {
        // Add infuser
        helper.setBlock(POS, RegistryEntries.BLOCK_BLOOD_INFUSER.get());
        BlockEntityBloodInfuser blockEntity = getInfuser(helper);
        blockEntity.getTank().setFluid(new FluidStack(RegistryEntries.FLUID_BLOOD, 5000));
        blockEntity.getInventory().setItem(BlockEntityBloodInfuser.SLOT_INFUSE, new ItemStack(RegistryEntries.ITEM_BOWL_OF_PROMISES_DUSTED));
        blockEntity.getInventory().setItem(BlockEntityBloodInfuser.SLOTS, new ItemStack(RegistryEntries.ITEM_PROMISE_TIER_3, 1));
        blockEntity.getInventory().setItem(BlockEntityBloodInfuser.SLOTS + 1, new ItemStack(RegistryEntries.ITEM_PROMISE_SPEED, 4));

        helper.succeedWhen(() -> {
            BlockEntityBloodInfuser infuser = getInfuser(helper);
            helper.assertFalse(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).isEmpty(), Component.literal("Result is not available"));
            helper.assertTrue(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).getItem() == RegistryEntries.ITEM_BOWL_OF_PROMISES_TIER3.get(), Component.literal("Result item is wrong"));
            helper.assertTrue(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).getCount() == 1, Component.literal("Result item count is wrong"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 200)
    public void testBloodInfuserDarkTank(GameTestHelper helper) {
        // Add infuser
        helper.setBlock(POS, RegistryEntries.BLOCK_BLOOD_INFUSER.get());
        BlockEntityBloodInfuser blockEntity = getInfuser(helper);
        blockEntity.getInventory().setItem(BlockEntityBloodInfuser.SLOT_INFUSE, new ItemStack(RegistryEntries.ITEM_DARK_TANK));
        blockEntity.getInventory().setItem(BlockEntityBloodInfuser.SLOTS, new ItemStack(RegistryEntries.ITEM_PROMISE_SPEED, 4));

        // Add tank above with blood
        helper.setBlock(POS.above(), RegistryEntries.BLOCK_DARK_TANK.get());
        BlockEntityDarkTank tank = helper.getBlockEntity(POS.above(), BlockEntityDarkTank.class);
        tank.getTank().setFluid(new FluidStack(RegistryEntries.FLUID_BLOOD, 16000));
        tank.setEnabled(true);

        helper.succeedWhen(() -> {
            BlockEntityBloodInfuser infuser = getInfuser(helper);
            helper.assertTrue(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE).isEmpty(), Component.literal("Infuse slot is not empty"));
            helper.assertFalse(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).isEmpty(), Component.literal("Result is not available"));
            helper.assertTrue(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).getItem() == RegistryEntries.ITEM_DARK_TANK.get(), Component.literal("Result item is wrong"));
            helper.assertValueEqual(16000, ItemAccess.forStack(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT)).getCapability(Capabilities.Fluid.ITEM).getAmountAsInt(0), Component.literal("Container content amount"));
            helper.assertValueEqual(RegistryEntries.FLUID_BLOOD.value(), ItemAccess.forStack(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT)).getCapability(Capabilities.Fluid.ITEM).getResource(0).getFluid(), Component.literal("Container content fluid"));
            helper.assertTrue(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).getCount() == 1, Component.literal("Result item count is wrong"));
            helper.assertValueEqual(0, infuser.getTank().getFluidAmount(), Component.literal("Blood infuser tank contents"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBloodInfuserBucket(GameTestHelper helper) {
        // Add infuser
        helper.setBlock(POS, RegistryEntries.BLOCK_BLOOD_INFUSER.get());
        BlockEntityBloodInfuser blockEntity = getInfuser(helper);
        blockEntity.getInventory().setItem(BlockEntityBloodInfuser.SLOT_INFUSE, new ItemStack(Items.BUCKET));
        blockEntity.getInventory().setItem(BlockEntityBloodInfuser.SLOTS, new ItemStack(RegistryEntries.ITEM_PROMISE_SPEED, 4));

        // Add tank above with blood
        helper.setBlock(POS.above(), RegistryEntries.BLOCK_DARK_TANK.get());
        BlockEntityDarkTank tank = helper.getBlockEntity(POS.above(), BlockEntityDarkTank.class);
        tank.getTank().setFluid(new FluidStack(RegistryEntries.FLUID_BLOOD, 1000));
        tank.setEnabled(true);

        helper.succeedWhen(() -> {
            BlockEntityBloodInfuser infuser = getInfuser(helper);
            helper.assertTrue(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE).isEmpty(), Component.literal("Infuse slot is not empty"));
            helper.assertFalse(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).isEmpty(), Component.literal("Result is not available"));
            helper.assertTrue(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).getItem() == RegistryEntries.ITEM_BUCKET_BLOOD.get(), Component.literal("Result item is wrong"));
            helper.assertTrue(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_INFUSE_RESULT).getCount() == 1, Component.literal("Result item count is wrong"));
            helper.assertValueEqual(0, infuser.getTank().getFluidAmount(), Component.literal("Blood infuser tank contents"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 200)
    public void testBloodInfuserEmptyFluidContainerBucket(GameTestHelper helper) {
        // Add infuser
        helper.setBlock(POS, RegistryEntries.BLOCK_BLOOD_INFUSER.get());
        BlockEntityBloodInfuser blockEntity = getInfuser(helper);
        blockEntity.getInventory().setItem(BlockEntityBloodInfuser.SLOT_CONTAINER, new ItemStack(RegistryEntries.ITEM_BUCKET_BLOOD));

        helper.succeedWhen(() -> {
            BlockEntityBloodInfuser infuser = getInfuser(helper);
            helper.assertTrue(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_CONTAINER).isEmpty(), Component.literal("Container was not drained"));
            helper.assertValueEqual(1000, infuser.getTank().getFluidAmount(), Component.literal("Blood infuser tank contents"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 200)
    public void testBloodInfuserEmptyCondensedBloodDrops(GameTestHelper helper) {
        // Add infuser
        helper.setBlock(POS, RegistryEntries.BLOCK_BLOOD_INFUSER.get());
        BlockEntityBloodInfuser blockEntity = getInfuser(helper);
        blockEntity.getInventory().setItem(BlockEntityBloodInfuser.SLOT_CONTAINER, new ItemStack(RegistryEntries.ITEM_CONDENSED_BLOOD, 10));

        helper.succeedWhen(() -> {
            BlockEntityBloodInfuser infuser = getInfuser(helper);
            helper.assertTrue(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_CONTAINER).isEmpty(), Component.literal("Container was not drained"));
            helper.assertValueEqual(5000, infuser.getTank().getFluidAmount(), Component.literal("Blood infuser tank contents"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 200)
    public void testBloodInfuserEmptyFluidContainerCreativeBloodDrop(GameTestHelper helper) {
        // Add infuser
        helper.setBlock(POS, RegistryEntries.BLOCK_BLOOD_INFUSER.get());
        BlockEntityBloodInfuser blockEntity = getInfuser(helper);
        blockEntity.getInventory().setItem(BlockEntityBloodInfuser.SLOT_CONTAINER, new ItemStack(RegistryEntries.ITEM_CREATIVE_BLOOD_DROP));

        helper.succeedWhen(() -> {
            BlockEntityBloodInfuser infuser = getInfuser(helper);
            helper.assertFalse(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_CONTAINER).isEmpty(), Component.literal("Container was incorrectly destroyed"));
            helper.assertValueEqual(10000, infuser.getTank().getFluidAmount(), Component.literal("Blood infuser tank contents"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 200)
    public void testBloodInfuserEmptyFluidContainerTank(GameTestHelper helper) {
        // Add infuser
        helper.setBlock(POS, RegistryEntries.BLOCK_BLOOD_INFUSER.get());
        BlockEntityBloodInfuser blockEntity = getInfuser(helper);
        ItemStack tankItem = new ItemStack(RegistryEntries.ITEM_DARK_TANK);
        try (var tx = Transaction.openRoot()) {
            tankItem.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(tankItem))
                    .insert(FluidResource.of(RegistryEntries.FLUID_BLOOD), 5000, tx);
            tx.commit();
        }
        blockEntity.getInventory().setItem(BlockEntityBloodInfuser.SLOT_CONTAINER, tankItem);

        helper.succeedWhen(() -> {
            BlockEntityBloodInfuser infuser = getInfuser(helper);
            helper.assertFalse(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_CONTAINER).isEmpty(), Component.literal("Container was incorrectly destroyed"));
            helper.assertValueEqual(ItemAccess.forStack(infuser.getInventory().getItem(BlockEntityBloodInfuser.SLOT_CONTAINER)).getCapability(Capabilities.Fluid.ITEM).getAmountAsInt(0), 0, Component.literal("Container contents"));
            helper.assertValueEqual(5000, infuser.getTank().getFluidAmount(), Component.literal("Blood infuser tank contents"));
        });
    }

    /**
     * Get the blood infuser at {@link #POS}.
     * This must be called again on every check inside {@link GameTestHelper#succeedWhen(Runnable)},
     * because the block entity instance is replaced when its chunk is reloaded while the test is running,
     * which leaves any earlier reference detached from the world and frozen in its last state.
     * @param helper The game test helper.
     * @return The blood infuser block entity.
     */
    protected static BlockEntityBloodInfuser getInfuser(GameTestHelper helper) {
        return helper.getBlockEntity(POS, BlockEntityBloodInfuser.class);
    }

}
