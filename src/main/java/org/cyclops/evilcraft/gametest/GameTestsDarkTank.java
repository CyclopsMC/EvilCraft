package org.cyclops.evilcraft.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerCapacity;
import org.cyclops.cyclopscore.gametest.GameTest;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;
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

    /**
     * Tests that placing a large Dark Tank item (64,000mB capacity, filled with Blood) as a block
     * preserves both the capacity and the fluid contents in the resulting block entity.
     */
    @GameTest(template = TEMPLATE_EMPTY)
    public void testPlaceLargeDarkTankItemPreservesCapacityAndFluid(GameTestHelper helper) {
        int largeCapacity = BlockEntityDarkTank.BASE_CAPACITY * 4; // 64,000 mB

        // Create a Dark Tank item with 64,000 mB capacity, filled with blood
        ItemAccess itemAccess = ItemAccess.forStack(new ItemStack(RegistryEntries.BLOCK_DARK_TANK.get()));
        IFluidHandlerCapacity fluidHandler = IModHelpersNeoForge.get().getFluidHelpers().getFluidHandlerItemCapacity(itemAccess).orElse(null);
        helper.assertTrue(fluidHandler != null, Component.literal("Dark Tank item should have a fluid handler capability"));
        try (var tx = Transaction.openRoot()) {
            fluidHandler.setTankCapacity(0, largeCapacity, tx);
            tx.commit();
        }
        try (var tx = Transaction.openRoot()) {
            fluidHandler.insert(FluidResource.of(RegistryEntries.FLUID_BLOOD), largeCapacity, tx);
            tx.commit();
        }
        ItemStack tankItem = itemAccess.getResource().toStack();

        // Place the Dark Tank block by simulating a player using BlockItem#place.
        // Click directly on the (air) block at POS — air is replaceable, so BlockPlaceContext
        // will set the placement position to absPos itself.
        BlockPos absPos = helper.absolutePos(POS);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, tankItem);
        BlockHitResult hitResult = new BlockHitResult(Vec3.atCenterOf(absPos), Direction.UP, absPos, false);
        BlockPlaceContext context = new BlockPlaceContext(helper.getLevel(), player, InteractionHand.MAIN_HAND, tankItem, hitResult);
        ((BlockItem) tankItem.getItem()).place(context);

        BlockEntityDarkTank tank = helper.getBlockEntity(POS, BlockEntityDarkTank.class);

        // Verify the block entity preserves the large capacity and full blood contents
        helper.assertValueEqual(tank.getTank().getCapacity(), largeCapacity, Component.literal("Placed Dark Tank should have large capacity"));
        helper.assertValueEqual(tank.getTank().getFluidAmount(), largeCapacity, Component.literal("Placed Dark Tank should contain all fluid"));
        helper.assertTrue(
                FluidStack.matches(tank.getTank().getFluid(), new FluidStack(RegistryEntries.FLUID_BLOOD, largeCapacity)),
                Component.literal("Placed Dark Tank should contain blood"));

        helper.succeed();
    }

}
