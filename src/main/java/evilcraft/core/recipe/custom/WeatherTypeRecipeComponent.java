package evilcraft.core.recipe.custom;

import evilcraft.core.weather.WeatherType;
import lombok.Data;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeInput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeOutput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeProperties;

/**
 * A recipe component that holds a {@link evilcraft.core.weather.WeatherType}.
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
