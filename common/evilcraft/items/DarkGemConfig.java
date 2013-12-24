package evilcraft.items;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig;

public class DarkGemConfig extends ExtendedConfig {
    
    public static DarkGemConfig _instance;

    public DarkGemConfig() {
        super(
            Reference.ITEM_DARKGEM,
            "Dark Gem",
            "darkgem",
            null,
            DarkGem.class
        );
    }
    
    @Override
    public void onRegistered() {
        OreDictionary.registerOre(getOreDictionaryId(), new ItemStack(DarkGem.getInstance()));
    }
    
    @Override
    public String getOreDictionaryId() {
        return "gemDark";
    }
    
}
