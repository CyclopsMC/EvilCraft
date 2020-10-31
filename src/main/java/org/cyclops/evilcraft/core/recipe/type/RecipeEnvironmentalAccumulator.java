package org.cyclops.evilcraft.core.recipe.type;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.weather.WeatherType;

/**
 * Environmental Accumulator recipe
 * @author rubensworks
 */
public class RecipeEnvironmentalAccumulator implements IRecipe<IInventory> {

    private final ResourceLocation id;
    private final Ingredient inputIngredient;
    private final WeatherType inputWeather;
    private final ItemStack outputItem;
    private final WeatherType outputWeather;
    private final int duration;
    private final int cooldownTime;
    private final float processingSpeed;

    public RecipeEnvironmentalAccumulator(ResourceLocation id,
                                          Ingredient inputIngredient, WeatherType inputWeather,
                                          ItemStack outputItem, WeatherType outputWeather,
                                          int duration, int cooldownTime, float processingSpeed) {
        this.id = id;
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

    public ItemStack getOutputItem() {
        return outputItem;
    }

    public WeatherType getOutputWeather() {
        return outputWeather;
    }

    public int getDuration() {
        return duration;
    }

    public int getCooldownTime() {
        return cooldownTime;
    }

    public float getProcessingSpeed() {
        return processingSpeed;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return inputIngredient.test(inv.getStackInSlot(0))
                && inputWeather.isActive(worldIn);
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        ItemStack inputStack = inv.getStackInSlot(0);
        ItemStack itemStack = getRecipeOutput().copy();
        if (!inputStack.isEmpty() && inputStack.hasTag()) {
            if(!itemStack.hasTag()) {
                itemStack.setTag(new CompoundNBT());
            }
            for (String key : inputStack.getTag().keySet()) {
                if(!itemStack.getTag().contains(key)) {
                    itemStack.getTag().put(key, inputStack.getTag().get(key));
                }
            }
        }
        return itemStack;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height <= 1;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.outputItem;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RegistryEntries.RECIPESERIALIZER_ENVIRONMENTAL_ACCUMULATOR;
    }

    @Override
    public IRecipeType<?> getType() {
        return RegistryEntries.RECIPETYPE_ENVIRONMENTAL_ACCUMULATOR;
    }
}
