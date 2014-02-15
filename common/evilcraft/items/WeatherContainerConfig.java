package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ItemConfig;

/**
 * Config for the {@link WeatherContainer}.
 * @author rubensworks
 *
 */
public class WeatherContainerConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static WeatherContainerConfig _instance;

    /**
     * Make a new instance.
     */
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
