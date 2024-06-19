package org.cyclops.evilcraft.core.recipe.type;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.biome.Biome;
import org.cyclops.cyclopscore.recipe.ItemStackFromIngredient;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.weather.WeatherType;
import org.cyclops.evilcraft.item.ItemBiomeExtractConfig;

import java.util.Optional;

/**
 * Environmental Accumulator recipe
 * @author rubensworks
 */
public class RecipeEnvironmentalAccumulatorBiomeExtract extends RecipeEnvironmentalAccumulator {

    public RecipeEnvironmentalAccumulatorBiomeExtract(Ingredient inputIngredient, WeatherType inputWeather, Either<ItemStack, ItemStackFromIngredient> outputItem, WeatherType outputWeather, Optional<Integer> duration, Optional<Integer> cooldownTime, Optional<Float> processingSpeed) {
        super(inputIngredient, inputWeather, outputItem, outputWeather, duration, cooldownTime, processingSpeed);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RegistryEntries.RECIPESERIALIZER_BIOME_EXTRACT.get();
    }

    @Override
    public ItemStack assemble(Inventory inventory, RegistryAccess registryAccess) {
        Biome biome = inventory.getWorld().getBiome(inventory.getPos()).value();
        Registry<Biome> biomeRegistry = inventory.getWorld().registryAccess().registry(Registries.BIOME).get();
        if (ItemBiomeExtractConfig.isCraftingBlacklisted(biomeRegistry, biome)) {
            return RegistryEntries.ITEM_BIOME_EXTRACT.get().createItemStack(biomeRegistry::getKey, null, 1);
        } else {
            return RegistryEntries.ITEM_BIOME_EXTRACT.get().createItemStack(biomeRegistry::getKey, biome, 1);
        }
    }
}
