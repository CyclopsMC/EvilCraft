package evilcraft.blocks;

import net.minecraft.item.ItemBlock;
import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ElementTypeCategory;
import evilcraft.api.config.configurable.ConfigurableProperty;
import evilcraft.items.ExcrementPileItemBlock;

/**
 * Config for the {@link ExcrementPile}.
 * @author rubensworks
 *
 */
public class ExcrementPileConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static ExcrementPileConfig _instance;
    
    /**
     * If Excrement can also poison any mob next to players.
     */
    @ConfigurableProperty(category = ElementTypeCategory.GENERAL, comment = "If Excrement can also poison any mob next to players.", isCommandable = true)
    public static boolean poisonEntities = false;

    /**
     * Make a new instance.
     */
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
    public Class<? extends ItemBlock> getItemBlockClass() {
        return ExcrementPileItemBlock.class;
    }
    
}
