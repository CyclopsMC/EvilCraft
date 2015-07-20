package org.cyclops.evilcraft.core.recipe.custom;

import lombok.Data;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeInput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeOutput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeProperties;
import org.cyclops.evilcraft.core.weather.WeatherType;

/**
 * A recipe component that holds a {@link WeatherType}.
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
