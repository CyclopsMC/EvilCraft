package org.cyclops.evilcraft.modcompat.jei.environmentalaccumulator;

import com.google.common.collect.Maps;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.core.weather.WeatherType;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Category for the Envir Acc recipes.
 * @author rubensworks
 */
public abstract class CommonEnvironmentalAccumulatorRecipeCategory implements IRecipeCategory {

    private final Map<WeatherType, IDrawableStatic> weatherIcons;
    private final Pair<Integer, Integer> weatherInPos;
    private final Pair<Integer, Integer> weatherOutPos;

    private EnvironmentalAccumulatorRecipeJEI lastRecipe = null;

    public CommonEnvironmentalAccumulatorRecipeCategory(IGuiHelper guiHelper, Pair<Integer, Integer> weatherInPos, Pair<Integer, Integer> weatherOutPos) {
        this.weatherInPos = weatherInPos;
        this.weatherOutPos = weatherOutPos;
        weatherIcons = Maps.newHashMap();
        ResourceLocation weatherResourceLocation = new ResourceLocation(Reference.MOD_ID + ":" + Reference.TEXTURE_PATH_GUI + "weathers.png");
        weatherIcons.put(WeatherType.CLEAR, guiHelper.createDrawable(weatherResourceLocation, 0, 0, 16, 16));
        weatherIcons.put(WeatherType.RAIN, guiHelper.createDrawable(weatherResourceLocation, 16, 0, 16, 16));
        weatherIcons.put(WeatherType.LIGHTNING, guiHelper.createDrawable(weatherResourceLocation, 32, 0, 16, 16));
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        if(lastRecipe != null) {
            if(lastRecipe.getInputWeather() != WeatherType.ANY) {
                weatherIcons.get(lastRecipe.getInputWeather()).draw(minecraft, weatherInPos.getLeft(), weatherInPos.getRight());
            }
            if(lastRecipe.getOutputWeather() != WeatherType.ANY) {
                weatherIcons.get(lastRecipe.getOutputWeather()).draw(minecraft, weatherOutPos.getLeft(), weatherOutPos.getRight());
            }
        }
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
        if(recipeWrapper instanceof EnvironmentalAccumulatorRecipeJEI) {
            EnvironmentalAccumulatorRecipeJEI recipe = (EnvironmentalAccumulatorRecipeJEI) recipeWrapper;
            this.lastRecipe = recipe;
        } else {
            this.lastRecipe = null;
        }
    }
}
