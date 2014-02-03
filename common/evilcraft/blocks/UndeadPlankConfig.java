package evilcraft.blocks;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;

public class UndeadPlankConfig extends BlockConfig {
    
    public static UndeadPlankConfig _instance;

    public UndeadPlankConfig() {
        super(
            Reference.BLOCK_UNDEADPLANK,
            "Undead Plank",
            "undeadPlank",
            null,
            UndeadPlank.class
        );
    }
    
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_WOODPLANK;
    }
    
    @Override
    public boolean isMultipartEnabled() {
        return true;
    }
    
}
