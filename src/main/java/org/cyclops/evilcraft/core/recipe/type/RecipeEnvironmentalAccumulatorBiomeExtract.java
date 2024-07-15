package org.cyclops.evilcraft.core.recipe.type;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
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
    public ItemStack assemble(Inventory inventory, HolderLookup.Provider registryAccess) {
        Holder<Biome> biome = inventory.getWorld().getBiome(inventory.getPos());
        if (ItemBiomeExtractConfig.isCraftingBlacklisted(biome)) {
            return RegistryEntries.ITEM_BIOME_EXTRACT.get().createItemStack(null, 1, registryAccess.lookupOrThrow(Registries.BIOME));
        } else {
            return RegistryEntries.ITEM_BIOME_EXTRACT.get().createItemStack(biome, 1, registryAccess.lookupOrThrow(Registries.BIOME));
        }
    }
}
