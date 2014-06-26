package evilcraft.api.recipes;

import net.minecraft.item.ItemStack;
import evilcraft.api.weather.WeatherType;
import evilcraft.blocks.EnvironmentalAccumulator;
import evilcraft.blocks.EnvironmentalAccumulatorConfig;

/**
 * Extension of a {@link CustomRecipe} so we can include weather types
 * in the recipes for the {@link EnvironmentalAccumulator}, aswel as the
 * speed with which items are being processed.
 * @author immortaleeb
 *
 */
public class EnvironmentalAccumulatorRecipe extends CustomRecipe {

    private WeatherType weatherType;
    private float processingSpeed;
    
    /**
     * Creates a new recipe for the environmental accumulator.
     * @param namedId The obligatory named id for this recipe.
     * @param itemStack Specifies the type of item that is thrown in the environmental accumulator.
     */
    public EnvironmentalAccumulatorRecipe(String namedId, ItemStack itemStack) {
        this(namedId, itemStack, null, 
                -1,
                -1f);
    }
    
    /**
     * Creates a new recipe for the environmental accumulator.
     * @param namedId The obligatory named id for this recipe.
     * @param itemStack Specifies the type of item that is thrown in the environmental accumulator.
     * @param weatherType Specifies the weather that is going on at the moment.
     */
    public EnvironmentalAccumulatorRecipe(String namedId, ItemStack itemStack, WeatherType weatherType) {
        this(namedId, itemStack, weatherType, 
                -1,
                -1f);
    }
    
    /**
     * Creates a new recipe for the environmental accumulator.
     * @param namedId The obligatory named id for this recipe.
     * @param itemStack Specifies the type of item that is thrown in the environmental accumulator.
     * @param duration The time it takes for the environmental accumulator to process this item.
     * @param processingSpeed The speed (in increments per tick) the item is processed.
     */
    public EnvironmentalAccumulatorRecipe(String namedId, ItemStack itemStack, int duration, float processingSpeed) {
        this(namedId, itemStack, null, duration, processingSpeed);
    }
    
    /**
     * Creates a new recipe for the environmental accumulator.
     * @param namedId The obligatory named id for this recipe.
     * @param itemStack Specifies the type of item that is thrown in the environmental accumulator.
     * @param weatherType Specifies the weather that is going on at the moment.
     * @param duration The time it takes for the environmental accumulator to process this item.
     * @param processingSpeed The speed (in increments per tick) the item is processed.
     */
    public EnvironmentalAccumulatorRecipe(String namedId, ItemStack itemStack, WeatherType weatherType, int duration, float processingSpeed) {
        super(namedId, itemStack, null, EnvironmentalAccumulator.getInstance(), duration);
        this.weatherType = weatherType;
        this.processingSpeed = processingSpeed;
    }

    /**
     * Returns the weather type.
     * @return {@link WeatherType} needed for this recipe.
     */
    public WeatherType getWeatherType() {
        return weatherType;
    }
    
    /**
     * Returns the processing speed, specified
     * as increments per tick.
     * @return The processing speed for this recipe.
     */
    public double getProcessingSpeed() {
        if (processingSpeed < 0)
            return EnvironmentalAccumulatorConfig.defaultProcessItemSpeed;
        
        return processingSpeed;
    }
    
    @Override
    public int getDuration() {
        int duration = super.getDuration();
        if (duration < 0)
            return EnvironmentalAccumulatorConfig.defaultProcessItemTickCount;
        
        return duration;
    }
    
    @Override
    public boolean equals(Object object) {
        if (object instanceof EnvironmentalAccumulatorRecipe) {
            EnvironmentalAccumulatorRecipe recipe = (EnvironmentalAccumulatorRecipe)object;
            
            // if weatherType == null we don't care about the weather so it just defaults to whatever weather you want
            return (super.equals(object) && (recipe.weatherType == weatherType || recipe.weatherType == null || weatherType == null));
        }
        
        return false;
    }
}
