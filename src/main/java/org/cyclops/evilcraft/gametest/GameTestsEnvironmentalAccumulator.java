package org.cyclops.evilcraft.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.gametest.GameTest;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityEnvironmentalAccumulator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class GameTestsEnvironmentalAccumulator {

    @GameTest(template = Reference.MOD_ID + ":empty10")
    public void testProcessingWithoutRecipeBookDisplay(GameTestHelper helper) throws Exception {
        testProcessing(helper, true);
    }

    @GameTest(template = Reference.MOD_ID + ":empty10")
    public void testProcessingWithRecipeBookDisplay(GameTestHelper helper) throws Exception {
        testProcessing(helper, false);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void testProcessing(GameTestHelper helper, boolean removeDisplay) throws Exception {
        var level = helper.getLevel();
        BlockPos pos = helper.absolutePos(new BlockPos(2, 1, 2));
        ItemStack input = new ItemStack(RegistryEntries.ITEM_WEATHER_CONTAINER.get(), 3);
        var recipe = IModHelpers.get().getCraftingHelpers()
                .findRecipes(level, RegistryEntries.RECIPETYPE_ENVIRONMENTAL_ACCUMULATOR.get()).stream()
                .filter(candidate -> candidate.value().getInputIngredient().test(input)
                        && (candidate.value().getInputWeather() == null
                        || candidate.value().getInputWeather().isActive(level)))
                .findFirst().orElseThrow();
        var manager = level.recipeAccess();
        Field cacheField = manager.getClass().getDeclaredField("recipeToDisplay");
        cacheField.setAccessible(true);
        Object originalCache = cacheField.get(manager);
        var entity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, input);
        try {
            if (removeDisplay) {
                var cache = new HashMap((Map) originalCache);
                cache.remove(recipe.id());
                cacheField.set(manager, cache);
                helper.assertTrue(IModHelpers.get().getCraftingHelpers()
                        .getRecipeDisplays(recipe.value().getType(), recipe.id()).isEmpty(),
                        Component.literal("The recipe-book display must be absent"));
            }
            level.addFreshEntity(entity);
            // Invoke only the idle transition: no multiblock, cooldown, or unrelated degradation ticks.
            var accumulator = new BlockEntityEnvironmentalAccumulator(pos,
                    RegistryEntries.BLOCK_ENVIRONMENTAL_ACCUMULATOR.get().defaultBlockState());
            accumulator.setLevel(level);
            Method idle = BlockEntityEnvironmentalAccumulator.class.getDeclaredMethod("updateEnvironmentalAccumulatorIdle");
            idle.setAccessible(true);
            idle.invoke(accumulator);
            helper.assertTrue(accumulator.getRecipe() != null, Component.literal("A matching recipe must start"));
            helper.assertTrue(accumulator.getRecipeDisplay() != null, Component.literal("The machine must retain its display"));
            helper.assertValueEqual(input.getCount(), 2, Component.literal("Exactly one dropped item must be consumed"));
            helper.assertValueEqual(accumulator.getInventory().getItem(0).getCount(), 3,
                    Component.literal("The existing input snapshot behavior must be preserved"));
        } finally {
            cacheField.set(manager, originalCache);
            entity.discard();
        }
        helper.succeed();
    }
}
