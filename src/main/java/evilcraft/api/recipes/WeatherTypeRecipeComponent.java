package evilcraft.api.recipes;

import evilcraft.api.weather.WeatherType;
import lombok.Data;

/**
 * A recipe component that holds a {@link evilcraft.api.weather.WeatherType}.
 * @author immortaleeb
 */
@Data
public class WeatherTypeRecipeComponent implements IRecipeInput, IRecipeOutput, IRecipeProperties, IWeatherTypeRecipeComponent {
    private final WeatherType weatherType;

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof WeatherTypeRecipeComponent)) return false;

        WeatherTypeRecipeComponent that = (WeatherTypeRecipeComponent)object;

        return this.weatherType.equals(that.weatherType)
                || this.weatherType == WeatherType.ANY
                || that.weatherType == WeatherType.ANY;
    }

    @Override
    public int hashCode() {
        return weatherType.hashCode() + 99;
    }
}
