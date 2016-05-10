package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

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
    public static boolean shapelessRecipes = true;

    /**
     * Make a new instance.
     */
    public WeatherContainerConfig() {
        super(
                EvilCraft._instance,
        	true,
            "weatherContainer",
            null,
            WeatherContainer.class
        );
    }
}
