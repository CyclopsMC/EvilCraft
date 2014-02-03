package evilcraft.blocks;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ConfigHandler;
import evilcraft.api.config.ElementTypeCategory;
import evilcraft.api.config.configurable.ConfigurableProperty;

public class DarkOreConfig extends BlockConfig {
    
    public static DarkOreConfig _instance;
    
    @ConfigurableProperty(category = ElementTypeCategory.OREGENERATION, comment = "How much ores per vein.")
    public static int blocksPerVein = 4;
    @ConfigurableProperty(category = ElementTypeCategory.OREGENERATION, comment = "How many veins per chunk.")
    public static int veinsPerChunk = 10;
    @ConfigurableProperty(category = ElementTypeCategory.OREGENERATION, comment = "Generation starts from this level.")
    public static int startY = 6;
    @ConfigurableProperty(category = ElementTypeCategory.OREGENERATION, comment = "Generation ends of this level.")
    public static int endY = 66;

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
