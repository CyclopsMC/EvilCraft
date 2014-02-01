package evilcraft.items;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import evilcraft.Reference;
import evilcraft.api.config.ItemConfig;

public class PoisonSacConfig extends ItemConfig {
    
    public static PoisonSacConfig _instance;

    public PoisonSacConfig() {
        super(
            Reference.ITEM_POISONSAC,
            "Poison Sac",
            "poisonSac",
            null,
            PoisonSac.class
        );
    }
    
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_MATERIALPOISONOUS;
    }
    
}
