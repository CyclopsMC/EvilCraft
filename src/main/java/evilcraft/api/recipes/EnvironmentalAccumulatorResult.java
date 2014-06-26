package evilcraft.api.recipes;

import net.minecraft.item.ItemStack;
import evilcraft.api.weather.WeatherType;
import evilcraft.blocks.EnvironmentalAccumulator;
import evilcraft.blocks.EnvironmentalAccumulatorConfig;
import evilcraft.entities.tileentities.environmentalaccumulator.IEAProcessingFinishedEffect;

/**
 * Extension of a {@link CustomRecipeResult} which adds a {@link WeatherType}
 * as an output for use in an {@link EnvironmentalAccumulator}.
 * @author immortaleeb
 *
 */
public class EnvironmentalAccumulatorResult extends CustomRecipeResult {
    
    private WeatherType weatherResult;
    private int cooldownTime;
    private IEAProcessingFinishedEffect finishedProcessingEffect;
    
    /**
     * Creates a new recipe result for an {@link EnvironmentalAccumulator}
     * @param recipe The input recipe.
     * @param itemResult Specifies the resulting item(s).
     */
    public EnvironmentalAccumulatorResult(EnvironmentalAccumulatorRecipe recipe, ItemStack itemResult) {
        this(recipe, itemResult, null, -1, null);
    }
    
    /**
     * Creates a new recipe result for an {@link EnvironmentalAccumulator}
     * @param recipe The input recipe.
     * @param itemResult Specifies the resulting item(s).
     * @param weatherResult The resulting weather.
     */
    public EnvironmentalAccumulatorResult(EnvironmentalAccumulatorRecipe recipe, ItemStack itemResult, WeatherType weatherResult) {
        this(recipe, itemResult, weatherResult, -1, null);
    }
    
    /**
     * Creates a new recipe result for an {@link EnvironmentalAccumulator}
     * @param recipe The input recipe.
     * @param itemResult Specifies the resulting item(s).
     * @param weatherResult The resulting weather.
     * @param finishedProcessingEffect The effect that has to be executed when we're done processing the item in this recipe.
     */
    public EnvironmentalAccumulatorResult(EnvironmentalAccumulatorRecipe recipe, ItemStack itemResult, WeatherType weatherResult,
            IEAProcessingFinishedEffect finishedProcessingEffect) {
        this(recipe, itemResult, weatherResult, -1, finishedProcessingEffect);
    }
    
    /**
     * Creates a new recipe result for an {@link EnvironmentalAccumulator}
     * @param recipe The input recipe.
     * @param itemResult Specifies the resulting item(s).
     * @param weatherResult The resulting weather.
     * @param cooldownTime The cooldown time (in number of ticks) needed after processing this item.
     * @param finishedProcessingEffect The effect that has to be executed when we're done processing the item in this recipe.
     */
    public EnvironmentalAccumulatorResult(EnvironmentalAccumulatorRecipe recipe, ItemStack itemResult, WeatherType weatherResult, 
            int cooldownTime, IEAProcessingFinishedEffect finishedProcessingEffect) {
        super(recipe, itemResult);
        this.weatherResult = weatherResult;
        this.cooldownTime = cooldownTime;
        this.finishedProcessingEffect = finishedProcessingEffect;
    }
    
    /**
     * Results the resulting items.
     * @return The resulting items.
     */
    public ItemStack getItemResult() {
        return getResult();
    }
    
    /**
     * Returns the result {@link WeatherType}.
     * @return The resulting {@link WeatherType}.
     */
    public WeatherType getWeatherResult() {
        return weatherResult;
    }
    
    /**
     * Returns the cooldown time in number of ticks.
     * @return The cooldown time for this recipe.
     */
    public int getCooldownTime() {
        if (cooldownTime < 0)
            return EnvironmentalAccumulatorConfig.defaultTickCooldown;
        
        return cooldownTime;
    }
    
    /**
     * Returns an {@link IEAProcessingFinishedEffect} which will
     * execute the effect that needs to be shown when processing the
     * item in this recipe finished.
     * @return The finished processing item effect.
     */
    public IEAProcessingFinishedEffect getFinishedProcessingEffect() {
        return finishedProcessingEffect;
    }
}
