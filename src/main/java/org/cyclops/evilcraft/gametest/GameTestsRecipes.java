package org.cyclops.evilcraft.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.CrafterBlockEntity;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import org.cyclops.cyclopscore.gametest.GameTest;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityDarkTank;
import org.cyclops.evilcraft.item.ItemBloodExtractorConfig;

public class GameTestsRecipes {

    public static final String TEMPLATE_EMPTY = Reference.MOD_ID + "empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 1, 2);

    @GameTest(template = TEMPLATE_EMPTY)
    public void testRecipesCombineBloodExtractorsEmpty(GameTestHelper helper) {
        // Set crafter
        helper.setBlock(POS, Blocks.CRAFTER);
        CrafterBlockEntity crafter = helper.getBlockEntity(POS, CrafterBlockEntity.class);
        helper.setBlock(POS.north(), Blocks.REDSTONE_TORCH);

        // Set recipe
        crafter.setItem(0, new ItemStack(RegistryEntries.ITEM_BLOOD_EXTRACTOR));
        crafter.setItem(1, new ItemStack(RegistryEntries.ITEM_BLOOD_EXTRACTOR));

        helper.succeedWhen(() -> {
            ItemStack result = helper.findOneEntity(EntityType.ITEM).getItem();
            helper.assertValueEqual(result.getItem(), RegistryEntries.ITEM_BLOOD_EXTRACTOR.get(), Component.literal("Result item is incorrect"));
            helper.assertValueEqual(result.get(org.cyclops.cyclopscore.RegistryEntries.COMPONENT_CAPACITY), ItemBloodExtractorConfig.containerSize * 2, Component.literal("Result item capacity is incorrect"));
            helper.assertTrue(result.get(org.cyclops.cyclopscore.RegistryEntries.COMPONENT_FLUID_CONTENT) == null, Component.literal("Result item fluid content is incorrect"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testRecipesCombineBloodExtractorsFilled(GameTestHelper helper) {
        // Set crafter
        helper.setBlock(POS, Blocks.CRAFTER);
        CrafterBlockEntity crafter = helper.getBlockEntity(POS, CrafterBlockEntity.class);
        helper.setBlock(POS.north(), Blocks.REDSTONE_TORCH);

        // Set recipe
        crafter.setItem(0, new ItemStack(RegistryEntries.ITEM_BLOOD_EXTRACTOR));
        crafter.setItem(1, new ItemStack(RegistryEntries.ITEM_BLOOD_EXTRACTOR));

        // Add some blood
        crafter.getItem(0).set(org.cyclops.cyclopscore.RegistryEntries.COMPONENT_FLUID_CONTENT, SimpleFluidContent.copyOf(new FluidStack(RegistryEntries.FLUID_BLOOD, 1000)));
        crafter.getItem(1).set(org.cyclops.cyclopscore.RegistryEntries.COMPONENT_FLUID_CONTENT, SimpleFluidContent.copyOf(new FluidStack(RegistryEntries.FLUID_BLOOD, 1000)));

        helper.succeedWhen(() -> {
            ItemStack result = helper.findOneEntity(EntityType.ITEM).getItem();
            helper.assertValueEqual(result.getItem(), RegistryEntries.ITEM_BLOOD_EXTRACTOR.get(), Component.literal("Result item is incorrect"));
            helper.assertValueEqual(result.get(org.cyclops.cyclopscore.RegistryEntries.COMPONENT_CAPACITY), ItemBloodExtractorConfig.containerSize * 2, Component.literal("Result item capacity is incorrect"));
            helper.assertValueEqual(result.get(org.cyclops.cyclopscore.RegistryEntries.COMPONENT_FLUID_CONTENT).getAmount(), 2000, Component.literal("Result item fluid content is incorrect"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testRecipesCombineDarkTanksEmpty(GameTestHelper helper) {
        // Set crafter
        helper.setBlock(POS, Blocks.CRAFTER);
        CrafterBlockEntity crafter = helper.getBlockEntity(POS, CrafterBlockEntity.class);
        helper.setBlock(POS.north(), Blocks.REDSTONE_TORCH);

        // Set recipe
        crafter.setItem(0, new ItemStack(RegistryEntries.ITEM_DARK_TANK));
        crafter.setItem(1, new ItemStack(RegistryEntries.ITEM_DARK_TANK));

        helper.succeedWhen(() -> {
            ItemStack result = helper.findOneEntity(EntityType.ITEM).getItem();
            helper.assertValueEqual(result.getItem(), RegistryEntries.ITEM_DARK_TANK.get(), Component.literal("Result item is incorrect"));
            helper.assertValueEqual(result.get(org.cyclops.cyclopscore.RegistryEntries.COMPONENT_CAPACITY), BlockEntityDarkTank.BASE_CAPACITY * 2, Component.literal("Result item capacity is incorrect"));
            helper.assertTrue(result.get(org.cyclops.cyclopscore.RegistryEntries.COMPONENT_FLUID_CONTENT) == null, Component.literal("Result item fluid content is incorrect"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testRecipesCombineDarkTanksFilled(GameTestHelper helper) {
        // Set crafter
        helper.setBlock(POS, Blocks.CRAFTER);
        CrafterBlockEntity crafter = helper.getBlockEntity(POS, CrafterBlockEntity.class);
        helper.setBlock(POS.north(), Blocks.REDSTONE_TORCH);

        // Set recipe
        crafter.setItem(0, new ItemStack(RegistryEntries.ITEM_DARK_TANK));
        crafter.setItem(1, new ItemStack(RegistryEntries.ITEM_DARK_TANK));

        // Add some blood
        crafter.getItem(0).set(org.cyclops.cyclopscore.RegistryEntries.COMPONENT_FLUID_CONTENT, SimpleFluidContent.copyOf(new FluidStack(RegistryEntries.FLUID_BLOOD, 1000)));
        crafter.getItem(1).set(org.cyclops.cyclopscore.RegistryEntries.COMPONENT_FLUID_CONTENT, SimpleFluidContent.copyOf(new FluidStack(RegistryEntries.FLUID_BLOOD, 1000)));

        helper.succeedWhen(() -> {
            ItemStack result = helper.findOneEntity(EntityType.ITEM).getItem();
            helper.assertValueEqual(result.getItem(), RegistryEntries.ITEM_DARK_TANK.get(), Component.literal("Result item is incorrect"));
            helper.assertValueEqual(result.get(org.cyclops.cyclopscore.RegistryEntries.COMPONENT_CAPACITY), BlockEntityDarkTank.BASE_CAPACITY * 2, Component.literal("Result item capacity is incorrect"));
            helper.assertValueEqual(result.get(org.cyclops.cyclopscore.RegistryEntries.COMPONENT_FLUID_CONTENT).getAmount(), 2000, Component.literal("Result item fluid content is incorrect"));
        });
    }

}
