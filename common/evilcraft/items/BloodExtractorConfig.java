package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ConfigHandler;
import evilcraft.api.config.ConfigurableProperty;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.events.LivingDeathEventHook;
import net.minecraftforge.common.MinecraftForge;

public class BloodExtractorConfig extends ExtendedConfig {
    
    public static BloodExtractorConfig _instance;
    
    @ConfigurableProperty(category = ConfigHandler.CATEGORY_GENERAL, comment = "The minimum amount of blood (mB) that can be extracted from this block.")
    public static int minMB = 1000;
    @ConfigurableProperty(category = ConfigHandler.CATEGORY_GENERAL, comment = "The maximum amount of blood (mB) that can be extracted from this block. IMPORTANT: must be larger than minMB!")
    public static int maxMB = 2000;

    public BloodExtractorConfig() {
        super(
            Reference.ITEM_BLOODEXTRACTOR,
            "Blood Extractor",
            "bloodextractor",
            null,
            BloodExtractor.class
        );
    }
    
}
