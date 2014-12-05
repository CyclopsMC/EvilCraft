package evilcraft.item;

import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Config for the {@link BloodContainer}.
 * @author rubensworks
 *
 */
@Deprecated
public class BloodContainerConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static BloodContainerConfig _instance;
    
    /**
     * Base container size in mB that will be multiplied every tier.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "The base amount of blood (mB) this container can hold * the level of container.", requiresMcRestart = true)
    public static int containerSizeBase = 5000;
    
    /**
     * The different containers.
     */
    public static String[] containerLevelNames = {"bloodCell", "bloodCan", "bloodBasin", "creativeBloodContainer"};

    /**
     * Make a new instance.
     */
    public BloodContainerConfig() {
        super(
        	true,
            "bloodContainer",
            null,
            BloodContainer.class
        );
    }
    
    /**
     * Get the amount of container tiers.
     * @return The container tiers.
     */
    public static int getContainerLevels() {
        return containerLevelNames.length;
    }
    
}
