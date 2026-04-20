package org.cyclops.evilcraft.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import org.cyclops.cyclopscore.gametest.GameTest;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityDarkTank;

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

        // Break the block to trigger the loot table (which calls LootFunctionCopyTankData)
        helper.destroyBlock(POS);

        helper.succeedWhen(() -> {
            ItemStack result = helper.findOneEntity(EntityType.ITEM).getItem();
            helper.assertValueEqual(result.getItem(), RegistryEntries.ITEM_DARK_TANK.get(), Component.literal("Dropped item should be a Dark Tank"));

            ItemAccess itemAccess = ItemAccess.forStack(result);
            ResourceHandler<FluidResource> fluidHandler = result.getCapability(Capabilities.Fluid.ITEM, itemAccess);
            helper.assertTrue(fluidHandler != null, Component.literal("Dropped Dark Tank should have a fluid handler"));
            helper.assertValueEqual(fluidHandler.getCapacityAsInt(0, FluidResource.EMPTY), largeCapacity, Component.literal("Dropped Dark Tank should have large capacity"));
            helper.assertValueEqual(fluidHandler.getAmountAsInt(0), largeCapacity, Component.literal("Dropped Dark Tank should contain all fluid"));
        });
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

        // Break the block
        helper.destroyBlock(POS);

        helper.succeedWhen(() -> {
            ItemStack result = helper.findOneEntity(EntityType.ITEM).getItem();
            helper.assertValueEqual(result.getItem(), RegistryEntries.ITEM_DARK_TANK.get(), Component.literal("Dropped item should be a Dark Tank"));

            ItemAccess itemAccess = ItemAccess.forStack(result);
            ResourceHandler<FluidResource> fluidHandler = result.getCapability(Capabilities.Fluid.ITEM, itemAccess);
            helper.assertTrue(fluidHandler != null, Component.literal("Dropped Dark Tank should have a fluid handler"));
            helper.assertValueEqual(fluidHandler.getCapacityAsInt(0, FluidResource.EMPTY), largeCapacity, Component.literal("Empty dropped Dark Tank should preserve large capacity"));
            helper.assertValueEqual(fluidHandler.getAmountAsInt(0), 0, Component.literal("Dropped Dark Tank should be empty"));
        });
    }

}
