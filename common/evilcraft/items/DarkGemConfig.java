package evilcraft.items;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import evilcraft.Reference;
import evilcraft.api.config.ItemConfig;

public class DarkGemConfig extends ItemConfig {
    
    public static DarkGemConfig _instance;

    public DarkGemConfig() {
        super(
            Reference.ITEM_DARKGEM,
            "Dark Gem",
            "darkGem",
            null,
            DarkGem.class
        );
    }
    
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_GEMDARK;
    }
    
}
