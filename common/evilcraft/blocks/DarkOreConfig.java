package evilcraft.blocks;

import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ElementTypeCategory;
import evilcraft.api.config.configurable.ConfigurableProperty;

/**
 * Config for the {@link DarkOre}.
 * @author rubensworks
 *
 */
public class DarkOreConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static DarkOreConfig _instance;
    
    /**
     * The amount of blocks per vein.
     */
    @ConfigurableProperty(category = ElementTypeCategory.OREGENERATION, comment = "How much ores per vein.")
    public static int blocksPerVein = 4;
    /**
     * The amount of veins per chunk.
     */
    @ConfigurableProperty(category = ElementTypeCategory.OREGENERATION, comment = "How many veins per chunk.")
    public static int veinsPerChunk = 10;
    /**
     * The start Y for ore spawning.
     */
    @ConfigurableProperty(category = ElementTypeCategory.OREGENERATION, comment = "Generation starts from this level.")
    public static int startY = 6;
    /**
     * The end Y for ore spawning.
     */
    @ConfigurableProperty(category = ElementTypeCategory.OREGENERATION, comment = "Generation ends of this level.")
    public static int endY = 66;

    /**
     * Make a new instance.
     */
    public DarkOreConfig() {
        super(
            Reference.BLOCK_DARKORE,
            "Dark Ore",
            "darkOre",
            null,
            DarkOre.class
        );
    }
    
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_OREDARK;
    }
    
}
