package evilcraft.item;

import evilcraft.core.config.ItemConfig;

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
        	true,
            "weatherContainer",
            null,
            WeatherContainer.class
        );
    }
    
}
