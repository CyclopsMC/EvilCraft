package evilcraft.item;

import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Config for the {@link BiomeExtract}.
 * @author rubensworks
 *
 */
public class BiomeExtractConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static BiomeExtractConfig _instance;

    /**
     * If creative versions for all variants should be added to the creative tab.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "If creative versions for all variants should be added to the creative tab.", requiresMcRestart = true)
    public static boolean creativeTabVariants = true;

    /**
     * If this should have recipes inside the Environmental Accumulator.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "If this should have recipes inside the Environmental Accumulator.", requiresMcRestart = true)
    public static boolean hasRecipes = true;

    /**
     * The cooldown time int the Environmental Accumulator recipe.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "The cooldown time int the Environmental Accumulator recipe.", requiresMcRestart = true)
    public static int envirAccCooldownTime = 50;

    /**
     * Make a new instance.
     */
    public BiomeExtractConfig() {
        super(
        	true,
            "biomeExtract",
            null,
            BiomeExtract.class
        );
    }

}
