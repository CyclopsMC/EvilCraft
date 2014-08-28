package evilcraft.api.recipes;

import evilcraft.api.weather.WeatherType;

/**
 * Interface for recipe components that hold a {@link evilcraft.api.weather.WeatherType}.
 * @author immortaleeb
 */
public interface IWeatherTypeRecipeComponent {
    /**
     * @return Returns the WeatherType held by this recipe component.
     */
    public WeatherType getWeatherType();
}
