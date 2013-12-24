package evilcraft.blocks;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import evilcraft.Reference;
import evilcraft.api.config.ConfigHandler;
import evilcraft.api.config.ConfigurableProperty;
import evilcraft.api.config.ExtendedConfig;

public class DarkOreConfig extends ExtendedConfig {
    
    public static DarkOreConfig _instance;
    
    @ConfigurableProperty(category = ConfigHandler.CATEGORY_OREGENERATION, comment = "How much ores per vein.")
    public static int blocksPerVein = 4;
    @ConfigurableProperty(category = ConfigHandler.CATEGORY_OREGENERATION, comment = "How many veins per chunk.")
    public static int veinsPerChunk = 10;
    @ConfigurableProperty(category = ConfigHandler.CATEGORY_OREGENERATION, comment = "Generation starts from this level.")
    public static int startY = 6;
    @ConfigurableProperty(category = ConfigHandler.CATEGORY_OREGENERATION, comment = "Generation ends of this level.")
    public static int endY = 66;

    public DarkOreConfig() {
        super(
            Reference.BLOCK_DARKORE,
            "Dark Ore",
            "darkore",
            null,
            DarkOre.class
        );
    }
    
    @Override
    public void onRegistered() {
        OreDictionary.registerOre(getOreDictionaryId(), new ItemStack(DarkOre.getInstance()));
    }
    
    @Override
    public String getOreDictionaryId() {
        return "oreDark";
    }
    
}
