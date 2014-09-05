package evilcraft.core.recipe.custom;

import evilcraft.core.weather.WeatherType;

/**
 * Interface for recipe components that hold a {@link evilcraft.core.weather.WeatherType}.
 * @author immortaleeb
 */
public interface IWeatherTypeRecipeComponent {
    /**
     * @return Returns the WeatherType held by this recipe component.
     */
    public WeatherType getWeatherType();
}
