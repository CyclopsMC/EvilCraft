package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ItemConfig;

public class WeatherContainerConfig extends ItemConfig {
    
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
