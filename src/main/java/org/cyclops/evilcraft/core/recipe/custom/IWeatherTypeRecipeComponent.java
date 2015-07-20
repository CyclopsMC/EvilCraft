package org.cyclops.evilcraft.core.recipe.custom;

import org.cyclops.evilcraft.core.weather.WeatherType;

/**
 * Interface for recipe components that hold a {@link WeatherType}.
 * @author immortaleeb
 */
public interface IWeatherTypeRecipeComponent {
    /**
     * @return Returns the WeatherType held by this recipe component.
     */
    public WeatherType getWeatherType();
}
