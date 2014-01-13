package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ElementTypeCategory;
import evilcraft.api.config.ItemConfig;
import evilcraft.api.config.configurable.ConfigurableProperty;

public class BloodExtractorConfig extends ItemConfig {
    
    public static BloodExtractorConfig _instance;
    
    @ConfigurableProperty(category = ElementTypeCategory.GENERAL, comment = "The minimum amount of blood (mB) that can be extracted from this block.")
    public static int minMB = 1000;
    @ConfigurableProperty(category = ElementTypeCategory.GENERAL, comment = "The maximum amount of blood (mB) that can be extracted from this block. IMPORTANT: must be larger than minMB!")
    public static int maxMB = 2000;

    public BloodExtractorConfig() {
        super(
            Reference.ITEM_BLOODEXTRACTOR,
            "Blood Extractor",
            "bloodExtractor",
            null,
            BloodExtractor.class
        );
    }
    
}
