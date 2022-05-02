package org.cyclops.evilcraft.core.recipe.type;

import com.mojang.datafixers.util.Either;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import org.cyclops.cyclopscore.recipe.ItemStackFromIngredient;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.weather.WeatherType;
import org.cyclops.evilcraft.item.ItemBiomeExtractConfig;

/**
 * Environmental Accumulator recipe
 * @author rubensworks
 */
public class RecipeEnvironmentalAccumulatorBiomeExtract extends RecipeEnvironmentalAccumulator {

    public RecipeEnvironmentalAccumulatorBiomeExtract(ResourceLocation id, Ingredient inputIngredient, WeatherType inputWeather, Either<ItemStack, ItemStackFromIngredient> outputItem, WeatherType outputWeather, int duration, int cooldownTime, float processingSpeed) {
        super(id, inputIngredient, inputWeather, outputItem, outputWeather, duration, cooldownTime, processingSpeed);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RegistryEntries.RECIPESERIALIZER_BIOME_EXTRACT;
    }

    @Override
    public ItemStack assemble(Inventory inventory) {
        Biome biome = inventory.getWorld().getBiome(inventory.getPos()).value();
        if (ItemBiomeExtractConfig.isCraftingBlacklisted(biome)) {
            return RegistryEntries.ITEM_BIOME_EXTRACT.createItemStack(null, 1);
        } else {
            return RegistryEntries.ITEM_BIOME_EXTRACT.createItemStack(biome, 1);
        }
    }
}
