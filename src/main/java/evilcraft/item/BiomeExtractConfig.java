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
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "If creative versions for all variants should be added to the creative tab.")
    public static boolean creativeTabVariants = true;

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
