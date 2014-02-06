package evilcraft.blocks;

import net.minecraft.item.ItemBlock;
import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ConfigHandler;
import evilcraft.api.config.ElementTypeCategory;
import evilcraft.api.config.configurable.ConfigurableProperty;
import evilcraft.items.ExcrementPileItemBlock;

public class ExcrementPileConfig extends BlockConfig {
    
    @ConfigurableProperty(category = ElementTypeCategory.GENERAL, comment = "If Excrement can also hurt any mob next to players.", isCommandable = true)
    public static boolean hurtEntities = false;
    
    public static ExcrementPileConfig _instance;

    public ExcrementPileConfig() {
        super(
            Reference.BLOCK_DARKBLOCK,
            "Excrement",
            "excrementPile",
            null,
            ExcrementPile.class
        );
    }
    
    @Override
    public boolean hasSubTypes() {
        return true;
    }
    
    @Override
    public Class<? extends ItemBlock> getItemBlockClass() {
        return ExcrementPileItemBlock.class;
    }
    
}
