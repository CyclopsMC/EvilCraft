package evilcraft.blocks;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig;

public class ExcrementPileConfig extends ExtendedConfig {
    
    public static ExcrementPileConfig _instance;

    public ExcrementPileConfig() {
        super(
            Reference.BLOCK_DARKBLOCK,
            "Excrement",
            "excrementpile",
            null,
            ExcrementPile.class
        );
    }
    
}
