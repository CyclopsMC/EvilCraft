package org.cyclops.evilcraft.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.cyclops.cyclopscore.gametest.GameTest;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityEnvironmentalAccumulator;
import org.cyclops.evilcraft.core.weather.WeatherType;
import org.cyclops.evilcraft.item.ItemWeatherContainer;

import java.util.concurrent.atomic.AtomicReference;

public class GameTestsEnvironmentalAccumulator {

    public static final String TEMPLATE_EMPTY = Reference.MOD_ID + ":empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 0, 2);

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 300)
    public void testFillWeatherContainer(GameTestHelper helper) {
        WeatherType.CLEAR.activate(helper.getLevel());
        helper.setBlock(POS, RegistryEntries.BLOCK_ENVIRONMENTAL_ACCUMULATOR.get());
        BlockEntityEnvironmentalAccumulator accumulator = helper.getBlockEntity(POS, BlockEntityEnvironmentalAccumulator.class);
        AtomicReference<ItemEntity> input = new AtomicReference<>();

        helper.startSequence()
                // Rain or thunder from earlier tests may still be fading out
                .thenWaitUntil(() -> helper.assertTrue(WeatherType.CLEAR.isActive(helper.getLevel()), "Weather did not become clear"))
                .thenExecute(() -> input.set(helper.spawnItem(RegistryEntries.ITEM_WEATHER_CONTAINER.get(), Vec3.atCenterOf(POS.above()))))
                .thenWaitUntil(() -> {
                    helper.assertTrue(input.get().isRemoved(), "Weather container was not consumed");
                    helper.assertTrue(accumulator.getRecipeDisplay() != null, "Recipe display was not set");
                })
                .thenWaitUntil(() -> helper.assertTrue(helper.getEntities(EntityTypes.ITEM, POS, 4).stream()
                        .anyMatch(entity -> ItemWeatherContainer.getWeatherType(entity.getItem()) == ItemWeatherContainer.WeatherContainerType.CLEAR),
                        "Filled weather container was not produced"))
                .thenExecute(() -> {
                    // Avoid degradation effects during cooldown
                    helper.setBlock(POS, Blocks.AIR);
                    WeatherType.CLEAR.activate(helper.getLevel());
                })
                .thenSucceed();
    }
}
