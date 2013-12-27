package evilcraft.blocks;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import evilcraft.Reference;
import evilcraft.api.config.ConfigHandler;
import evilcraft.api.config.ConfigurableProperty;
import evilcraft.api.config.ExtendedConfig;

public class ExcrementPileConfig extends ExtendedConfig {
    
    @ConfigurableProperty(category = ConfigHandler.CATEGORY_GENERAL, comment = "If Excrement can also hurt any mob next to players.")
    public static boolean hurtEntities = true;
    
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
