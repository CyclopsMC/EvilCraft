package evilcraft.blocks;

import evilcraft.api.config.ConfigHandler;
import evilcraft.api.config.ConfigurableProperty;
import evilcraft.api.config.ExtendedConfig;

public class DarkOreConfig extends ExtendedConfig {
    
    public static DarkOreConfig _instance;
    
    @ConfigurableProperty(category = ConfigHandler.CATEGORY_GENERAL, comment = "How much ores per vein.")
    public static int blocksPerVein = 4;

    public DarkOreConfig() {
        super(
            3843,
            "Dark Ore",
            "darkore",
            null,
            DarkOre.class
        );
    }
    
}
