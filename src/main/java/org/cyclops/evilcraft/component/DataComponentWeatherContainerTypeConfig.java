package org.cyclops.evilcraft.component;

import org.cyclops.cyclopscore.config.extendedconfig.DataComponentConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.item.ItemWeatherContainer;

/**
 * @author rubensworks
 */
public class DataComponentWeatherContainerTypeConfig extends DataComponentConfigCommon<ItemWeatherContainer.WeatherContainerType, IModBase> {
    public DataComponentWeatherContainerTypeConfig() {
        super(EvilCraft._instance, "weather_container_type", builder -> builder
                .persistent(ItemWeatherContainer.WeatherContainerType.CODEC)
                .networkSynchronized(ItemWeatherContainer.WeatherContainerType.STREAM_CODEC));
    }
}
