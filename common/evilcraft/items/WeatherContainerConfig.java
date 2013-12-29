package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig;

public class WeatherContainerConfig extends ExtendedConfig {
    
    public static WeatherContainerConfig _instance;

    public WeatherContainerConfig() {
        super(
            Reference.ITEM_WEATHERCONTAINER,
            "Weather Container",
            "weatherContainer",
            null,
            WeatherContainer.class
        );
    }
    
}
