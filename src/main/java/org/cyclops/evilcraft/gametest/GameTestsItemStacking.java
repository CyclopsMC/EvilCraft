package org.cyclops.evilcraft.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.cyclops.cyclopscore.gametest.GameTest;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.helper.ItemHelpers;

public class GameTestsItemStacking {

    public static final String TEMPLATE_EMPTY = Reference.MOD_ID + ":empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 0, 2);

    @GameTest(template = TEMPLATE_EMPTY)
    public void testStackingBloodExtractorEmpty(GameTestHelper helper) {
        // Let hopper feed into chest
        helper.setBlock(POS.above(), Blocks.HOPPER);
        helper.setBlock(POS, Blocks.CHEST);

        // Add items to hopper
        HopperBlockEntity hopper = helper.getBlockEntity(POS.above(), HopperBlockEntity.class);
        hopper.setItem(0, new ItemStack(RegistryEntries.ITEM_BLOOD_EXTRACTOR, 3));
        ChestBlockEntity chest = helper.getBlockEntity(POS, ChestBlockEntity.class);

        helper.succeedWhen(() -> {
            // Check result remains in the same stack
            helper.assertTrue(chest.getItem(0).getCount() == 3, Component.literal("Stacking failed"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testStackingBloodExtractorFilled(GameTestHelper helper) {
        // Let hopper feed into chest
        helper.setBlock(POS.above(), Blocks.HOPPER);
        helper.setBlock(POS, Blocks.CHEST);

        // Add items to hopper
        HopperBlockEntity hopper = helper.getBlockEntity(POS.above(), HopperBlockEntity.class);
        ItemStack itemStack = new ItemStack(RegistryEntries.ITEM_BLOOD_EXTRACTOR);
        ResourceHandler<FluidResource> fluidHandler = itemStack.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(itemStack));
        try (var tx = Transaction.openRoot()) {
            fluidHandler.insert(FluidResource.of(RegistryEntries.FLUID_BLOOD), 1000, tx);
        }
        hopper.setItem(0, itemStack.copy());
        hopper.setItem(1, itemStack.copy());
        hopper.setItem(2, itemStack.copy());
        ChestBlockEntity chest = helper.getBlockEntity(POS, ChestBlockEntity.class);

        helper.succeedWhen(() -> {
            // Check result remains in the same stack
            helper.assertTrue(chest.getItem(0).getCount() == 3, Component.literal("Stacking failed"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testStackingBloodExtractorFilledAndDrained(GameTestHelper helper) {
        // Let hopper feed into chest
        helper.setBlock(POS.above(), Blocks.HOPPER);
        helper.setBlock(POS, Blocks.CHEST);

        // Add items to hopper
        HopperBlockEntity hopper = helper.getBlockEntity(POS.above(), HopperBlockEntity.class);
        ItemStack itemStack1 = new ItemStack(RegistryEntries.ITEM_BLOOD_EXTRACTOR);
        ItemStack itemStack2 = new ItemStack(RegistryEntries.ITEM_BLOOD_EXTRACTOR);
        ItemStack itemStack3 = new ItemStack(RegistryEntries.ITEM_BLOOD_EXTRACTOR);
        ResourceHandler<FluidResource> fluidHandler = itemStack2.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(itemStack2));
        try (var tx = Transaction.openRoot()) {
            fluidHandler.insert(FluidResource.of(RegistryEntries.FLUID_BLOOD), 1000, tx);
            fluidHandler.extract(FluidResource.of(RegistryEntries.FLUID_BLOOD), 1000, tx);
        }
        hopper.setItem(0, itemStack1);
        hopper.setItem(1, itemStack2);
        hopper.setItem(2, itemStack3);
        ChestBlockEntity chest = helper.getBlockEntity(POS, ChestBlockEntity.class);

        helper.succeedWhen(() -> {
            // Check result remains in the same stack
            helper.assertTrue(chest.getItem(0).getCount() == 3, Component.literal("Stacking failed"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testStackingBloodExtractorFilledToggled(GameTestHelper helper) {
        // Let hopper feed into chest
        helper.setBlock(POS.above(), Blocks.HOPPER);
        helper.setBlock(POS, Blocks.CHEST);

        // Add items to hopper
        HopperBlockEntity hopper = helper.getBlockEntity(POS.above(), HopperBlockEntity.class);
        ItemStack itemStack = new ItemStack(RegistryEntries.ITEM_BLOOD_EXTRACTOR);
        ResourceHandler<FluidResource> fluidHandler = itemStack.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(itemStack));
        try (var tx = Transaction.openRoot()) {
            fluidHandler.insert(FluidResource.of(RegistryEntries.FLUID_BLOOD), 1000, tx);
        }
        ItemHelpers.toggleActivation(itemStack);
        hopper.setItem(0, itemStack.copy());
        hopper.setItem(1, itemStack.copy());
        hopper.setItem(2, itemStack.copy());
        ChestBlockEntity chest = helper.getBlockEntity(POS, ChestBlockEntity.class);

        helper.succeedWhen(() -> {
            // Check result remains in the same stack
            helper.assertTrue(chest.getItem(0).getCount() == 3, Component.literal("Stacking failed"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testStackingBloodExtractorToggledUntoggled(GameTestHelper helper) {
        // Let hopper feed into chest
        helper.setBlock(POS.above(), Blocks.HOPPER);
        helper.setBlock(POS, Blocks.CHEST);

        // Add items to hopper
        HopperBlockEntity hopper = helper.getBlockEntity(POS.above(), HopperBlockEntity.class);
        ItemStack itemStack1 = new ItemStack(RegistryEntries.ITEM_BLOOD_EXTRACTOR);
        ItemStack itemStack2 = new ItemStack(RegistryEntries.ITEM_BLOOD_EXTRACTOR);
        ItemStack itemStack3 = new ItemStack(RegistryEntries.ITEM_BLOOD_EXTRACTOR);
        ItemHelpers.toggleActivation(itemStack2);
        ItemHelpers.toggleActivation(itemStack2);
        hopper.setItem(0, itemStack1);
        hopper.setItem(1, itemStack2);
        hopper.setItem(2, itemStack3);
        ChestBlockEntity chest = helper.getBlockEntity(POS, ChestBlockEntity.class);

        helper.succeedWhen(() -> {
            // Check result remains in the same stack
            helper.assertTrue(chest.getItem(0).getCount() == 3, Component.literal("Stacking failed"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testStackingDarkTankEmpty(GameTestHelper helper) {
        // Let hopper feed into chest
        helper.setBlock(POS.above(), Blocks.HOPPER);
        helper.setBlock(POS, Blocks.CHEST);

        // Add items to hopper
        HopperBlockEntity hopper = helper.getBlockEntity(POS.above(), HopperBlockEntity.class);
        hopper.setItem(0, new ItemStack(RegistryEntries.ITEM_DARK_TANK, 3));
        ChestBlockEntity chest = helper.getBlockEntity(POS, ChestBlockEntity.class);

        helper.succeedWhen(() -> {
            // Check result remains in the same stack
            helper.assertTrue(chest.getItem(0).getCount() == 3, Component.literal("Stacking failed"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testStackingDarkTankFilled(GameTestHelper helper) {
        // Let hopper feed into chest
        helper.setBlock(POS.above(), Blocks.HOPPER);
        helper.setBlock(POS, Blocks.CHEST);

        // Add items to hopper
        HopperBlockEntity hopper = helper.getBlockEntity(POS.above(), HopperBlockEntity.class);
        ItemStack itemStack = new ItemStack(RegistryEntries.ITEM_DARK_TANK);
        ResourceHandler<FluidResource> fluidHandler = itemStack.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(itemStack));
        try (var tx = Transaction.openRoot()) {
            fluidHandler.insert(FluidResource.of(RegistryEntries.FLUID_BLOOD), 1000, tx);
        }
        hopper.setItem(0, itemStack.copy());
        hopper.setItem(1, itemStack.copy());
        hopper.setItem(2, itemStack.copy());
        ChestBlockEntity chest = helper.getBlockEntity(POS, ChestBlockEntity.class);

        helper.succeedWhen(() -> {
            // Check result remains in the same stack
            helper.assertTrue(chest.getItem(0).getCount() == 3, Component.literal("Stacking failed"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testStackingDarkTankFilledAndDrained(GameTestHelper helper) {
        // Let hopper feed into chest
        helper.setBlock(POS.above(), Blocks.HOPPER);
        helper.setBlock(POS, Blocks.CHEST);

        // Add items to hopper
        HopperBlockEntity hopper = helper.getBlockEntity(POS.above(), HopperBlockEntity.class);
        ItemStack itemStack1 = new ItemStack(RegistryEntries.ITEM_DARK_TANK);
        ItemStack itemStack2 = new ItemStack(RegistryEntries.ITEM_DARK_TANK);
        ItemStack itemStack3 = new ItemStack(RegistryEntries.ITEM_DARK_TANK);
        ResourceHandler<FluidResource> fluidHandler = itemStack2.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(itemStack2));
        try (var tx = Transaction.openRoot()) {
            fluidHandler.insert(FluidResource.of(RegistryEntries.FLUID_BLOOD), 1000, tx);
            fluidHandler.extract(FluidResource.of(RegistryEntries.FLUID_BLOOD), 1000, tx);
        }
        hopper.setItem(0, itemStack1);
        hopper.setItem(1, itemStack2);
        hopper.setItem(2, itemStack3);
        ChestBlockEntity chest = helper.getBlockEntity(POS, ChestBlockEntity.class);

        helper.succeedWhen(() -> {
            // Check result remains in the same stack
            helper.assertTrue(chest.getItem(0).getCount() == 3, Component.literal("Stacking failed"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testStackingDarkTankFilledToggled(GameTestHelper helper) {
        // Let hopper feed into chest
        helper.setBlock(POS.above(), Blocks.HOPPER);
        helper.setBlock(POS, Blocks.CHEST);

        // Add items to hopper
        HopperBlockEntity hopper = helper.getBlockEntity(POS.above(), HopperBlockEntity.class);
        ItemStack itemStack = new ItemStack(RegistryEntries.ITEM_DARK_TANK);
        ResourceHandler<FluidResource> fluidHandler = itemStack.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(itemStack));
        try (var tx = Transaction.openRoot()) {
            fluidHandler.insert(FluidResource.of(RegistryEntries.FLUID_BLOOD), 1000, tx);
        }
        ItemHelpers.toggleActivation(itemStack);
        hopper.setItem(0, itemStack.copy());
        hopper.setItem(1, itemStack.copy());
        hopper.setItem(2, itemStack.copy());
        ChestBlockEntity chest = helper.getBlockEntity(POS, ChestBlockEntity.class);

        helper.succeedWhen(() -> {
            // Check result remains in the same stack
            helper.assertTrue(chest.getItem(0).getCount() == 3, Component.literal("Stacking failed"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testStackingDarkTankToggledUntoggled(GameTestHelper helper) {
        // Let hopper feed into chest
        helper.setBlock(POS.above(), Blocks.HOPPER);
        helper.setBlock(POS, Blocks.CHEST);

        // Add items to hopper
        HopperBlockEntity hopper = helper.getBlockEntity(POS.above(), HopperBlockEntity.class);
        ItemStack itemStack1 = new ItemStack(RegistryEntries.ITEM_DARK_TANK);
        ItemStack itemStack2 = new ItemStack(RegistryEntries.ITEM_DARK_TANK);
        ItemStack itemStack3 = new ItemStack(RegistryEntries.ITEM_DARK_TANK);
        ItemHelpers.toggleActivation(itemStack2);
        ItemHelpers.toggleActivation(itemStack2);
        hopper.setItem(0, itemStack1);
        hopper.setItem(1, itemStack2);
        hopper.setItem(2, itemStack3);
        ChestBlockEntity chest = helper.getBlockEntity(POS, ChestBlockEntity.class);

        helper.succeedWhen(() -> {
            // Check result remains in the same stack
            helper.assertTrue(chest.getItem(0).getCount() == 3, Component.literal("Stacking failed"));
        });
    }

}
