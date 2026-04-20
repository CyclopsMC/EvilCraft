package org.cyclops.evilcraft.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import org.cyclops.cyclopscore.gametest.GameTest;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityDarkTank;

import java.util.List;

public class GameTestsDarkTank {

    public static final String TEMPLATE_EMPTY = Reference.MOD_ID + ":empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 1, 2);

    /**
     * Tests that breaking a large Dark Tank (144,000mB capacity) drops an item that preserves
     * the full capacity and fluid amount, not just the base 16,000mB capacity.
     */
    @GameTest(template = TEMPLATE_EMPTY)
    public void testBreakLargeDarkTankFullPreservesCapacityAndFluid(GameTestHelper helper) {
        int largeCapacity = BlockEntityDarkTank.BASE_CAPACITY * 9;

        // Place a Dark Tank
        helper.setBlock(POS, RegistryEntries.BLOCK_DARK_TANK.get());
        BlockEntityDarkTank tank = helper.getBlockEntity(POS, BlockEntityDarkTank.class);

        // Set up as a large tank filled with blood
        tank.getTank().setCapacity(largeCapacity);
        tank.getTank().setFluid(new FluidStack(RegistryEntries.FLUID_BLOOD, largeCapacity));

        // Get the drops via the loot table (which invokes LootFunctionCopyTankData)
        BlockPos absPos = helper.absolutePos(POS);
        List<ItemStack> drops = Block.getDrops(helper.getBlockState(POS), helper.getLevel(), absPos, tank);

        helper.assertTrue(!drops.isEmpty(), Component.literal("Breaking a Dark Tank should produce drops"));
        ItemStack result = drops.get(0);
        helper.assertValueEqual(result.getItem(), RegistryEntries.ITEM_DARK_TANK.get(), Component.literal("Dropped item should be a Dark Tank"));

        ItemAccess itemAccess = ItemAccess.forStack(result);
        ResourceHandler<FluidResource> fluidHandler = result.getCapability(Capabilities.Fluid.ITEM, itemAccess);
        helper.assertTrue(fluidHandler != null, Component.literal("Dropped Dark Tank should have a fluid handler"));
        helper.assertValueEqual(fluidHandler.getCapacityAsInt(0, FluidResource.EMPTY), largeCapacity, Component.literal("Dropped Dark Tank should have large capacity"));
        helper.assertValueEqual(fluidHandler.getAmountAsInt(0), largeCapacity, Component.literal("Dropped Dark Tank should contain all fluid"));

        helper.succeed();
    }

    /**
     * Tests that breaking an empty large Dark Tank preserves the large capacity.
     */
    @GameTest(template = TEMPLATE_EMPTY)
    public void testBreakEmptyLargeDarkTankPreservesCapacity(GameTestHelper helper) {
        int largeCapacity = BlockEntityDarkTank.BASE_CAPACITY * 9;

        // Place a Dark Tank
        helper.setBlock(POS, RegistryEntries.BLOCK_DARK_TANK.get());
        BlockEntityDarkTank tank = helper.getBlockEntity(POS, BlockEntityDarkTank.class);

        // Set up as a large tank with no fluid
        tank.getTank().setCapacity(largeCapacity);

        // Get the drops via the loot table (which invokes LootFunctionCopyTankData)
        BlockPos absPos = helper.absolutePos(POS);
        List<ItemStack> drops = Block.getDrops(helper.getBlockState(POS), helper.getLevel(), absPos, tank);

        helper.assertTrue(!drops.isEmpty(), Component.literal("Breaking a Dark Tank should produce drops"));
        ItemStack result = drops.get(0);
        helper.assertValueEqual(result.getItem(), RegistryEntries.ITEM_DARK_TANK.get(), Component.literal("Dropped item should be a Dark Tank"));

        ItemAccess itemAccess = ItemAccess.forStack(result);
        ResourceHandler<FluidResource> fluidHandler = result.getCapability(Capabilities.Fluid.ITEM, itemAccess);
        helper.assertTrue(fluidHandler != null, Component.literal("Dropped Dark Tank should have a fluid handler"));
        helper.assertValueEqual(fluidHandler.getCapacityAsInt(0, FluidResource.EMPTY), largeCapacity, Component.literal("Empty dropped Dark Tank should preserve large capacity"));
        helper.assertValueEqual(fluidHandler.getAmountAsInt(0), 0, Component.literal("Dropped Dark Tank should be empty"));

        helper.succeed();
    }

}
