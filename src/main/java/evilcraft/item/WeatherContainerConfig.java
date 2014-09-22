package evilcraft.item;

import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.extendedconfig.ItemConfig;

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
     * If shapeless crafting of the higher tiers of weather containers should be enabled.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.GENERAL, comment = "If shapeless crafting of the higher tiers of weather containers should be enabled.", requiresMcRestart = true)
    public static boolean shapelessWeatherContainerRecipes = true;

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
