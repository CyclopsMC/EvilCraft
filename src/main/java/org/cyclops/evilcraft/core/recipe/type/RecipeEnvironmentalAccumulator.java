package org.cyclops.evilcraft.core.recipe.type;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import org.cyclops.cyclopscore.recipe.ItemStackFromIngredient;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockEnvironmentalAccumulatorConfig;
import org.cyclops.evilcraft.core.recipe.display.RecipeDisplayEnvironmentalAccumulator;
import org.cyclops.evilcraft.core.weather.WeatherType;

import java.util.List;
import java.util.Optional;

/**
 * Environmental Accumulator recipe
 * @author rubensworks
 */
public class RecipeEnvironmentalAccumulator implements Recipe<RecipeEnvironmentalAccumulator.Inventory> {

    private final Ingredient inputIngredient;
    private final WeatherType inputWeather;
    private final Either<ItemStackTemplate, ItemStackFromIngredient> outputItem;
    private final WeatherType outputWeather;
    private final Optional<Integer> duration;
    private final Optional<Integer> cooldownTime;
    private final Optional<Float> processingSpeed;

    private PlacementInfo placementInfo;

    public RecipeEnvironmentalAccumulator(Ingredient inputIngredient, WeatherType inputWeather,
                                          Either<ItemStackTemplate, ItemStackFromIngredient> outputItem, WeatherType outputWeather,
                                          Optional<Integer> duration, Optional<Integer> cooldownTime, Optional<Float> processingSpeed) {
        this.inputIngredient = inputIngredient;
        this.inputWeather = inputWeather;
        this.outputItem = outputItem;
        this.outputWeather = outputWeather;
        this.duration = duration;
        this.cooldownTime = cooldownTime;
        this.processingSpeed = processingSpeed;
    }

    public Ingredient getInputIngredient() {
        return inputIngredient;
    }

    public WeatherType getInputWeather() {
        return inputWeather;
    }

    public Either<ItemStackTemplate, ItemStackFromIngredient> getOutputItem() {
        return outputItem;
    }

    public ItemStack getOutputItemFirst() {
        return getOutputItem().map(ItemStackTemplate::create, ItemStackFromIngredient::getFirstItemStack);
    }

    public WeatherType getOutputWeather() {
        return outputWeather;
    }

    public Optional<Integer> getDurationRaw() {
        return this.duration;
    }

    public Optional<Integer> getCooldownTimeRaw() {
        return this.cooldownTime;
    }

    public Optional<Float> getProcessingSpeedRaw() {
        return this.processingSpeed;
    }

    public int getDuration() {
        int duration = getDurationRaw().orElse(-1);
        // Note: we need to do this because defaultProcessItemTickCount is set AFTER the recipes are created
        if (duration < 0)
            return BlockEnvironmentalAccumulatorConfig.defaultProcessItemTickCount;

        return duration;
    }

    public int getCooldownTime() {
        int cooldownTime = getCooldownTimeRaw().orElse(-1);
        // Note: we need to do this because defaultProcessItemTickCount is set AFTER the recipes are created
        if (cooldownTime < 0)
            return BlockEnvironmentalAccumulatorConfig.defaultTickCooldown;

        return cooldownTime;
    }

    public float getProcessingSpeed() {
        float processingSpeed = getProcessingSpeedRaw().orElse(-1.0F);
        // Note: we need to do this because defaultProcessItemSpeed is set AFTER the recipes are created
        if (processingSpeed < 0)
            return (float) BlockEnvironmentalAccumulatorConfig.defaultProcessItemSpeed;

        return processingSpeed;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public boolean matches(RecipeEnvironmentalAccumulator.Inventory inv, Level worldIn) {
        return inputIngredient.test(inv.getItem(0))
                && inputWeather.isActive(worldIn);
    }

    @Override
    public ItemStack assemble(RecipeEnvironmentalAccumulator.Inventory inv) {
        ItemStack inputStack = inv.getItem(0);
        ItemStack itemStack = getOutputItemFirst().copy();
        if (!inputStack.isEmpty()) {
            for (DataComponentType<?> dataComponentType : inputStack.getComponents().keySet()) {
                if (dataComponentType != RegistryEntries.COMPONENT_WEATHER_CONTAINER_TYPE.value()) {
                    itemStack.set((DataComponentType) dataComponentType, inputStack.get(dataComponentType));
                }
            }
        }
        return itemStack;
    }

    public ItemStack getResultItem(net.minecraft.core.HolderLookup.Provider registryAccess) {
        return this.getOutputItemFirst().copy();
    }

    @Override
    public RecipeSerializer<? extends Recipe<Inventory>> getSerializer() {
        return RegistryEntries.RECIPESERIALIZER_ENVIRONMENTAL_ACCUMULATOR.get();
    }

    @Override
    public RecipeType<? extends Recipe<Inventory>> getType() {
        return RegistryEntries.RECIPETYPE_ENVIRONMENTAL_ACCUMULATOR.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        if (this.placementInfo == null) {
            this.placementInfo = PlacementInfo.create(this.inputIngredient);
        }
        return this.placementInfo;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RegistryEntries.RECIPEBOOKCATEGORY_ENVIRONMENTAL_ACCUMULATOR.get();
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(new RecipeDisplayEnvironmentalAccumulator(
                this.getInputIngredient().display(),
                this.getInputWeather(),
                new SlotDisplay.ItemStackSlotDisplay(this.getOutputItem().map(l -> l, i -> ItemStackTemplate.fromNonEmptyStack(i.getFirstItemStack()))),
                this.getOutputWeather(),
                new SlotDisplay.ItemSlotDisplay(RegistryEntries.BLOCK_ENVIRONMENTAL_ACCUMULATOR.get().asItem()),
                this.getDuration(),
                this.getCooldownTime(),
                this.getProcessingSpeed()
        ));
    }

    public static interface Inventory extends RecipeInput {
        public Level getWorld();
        public BlockPos getPos();
    }

    public static class InventoryDummy implements Inventory {
        private final List<ItemStack> itemStacks;

        public InventoryDummy(ItemStack... stacksIn) {
            this.itemStacks = Lists.newArrayList(stacksIn);
        }

        @Override
        public Level getWorld() {
            return null;
        }

        @Override
        public BlockPos getPos() {
            return null;
        }

        @Override
        public ItemStack getItem(int i) {
            return itemStacks.get(i);
        }

        @Override
        public int size() {
            return itemStacks.size();
        }
    }

}
