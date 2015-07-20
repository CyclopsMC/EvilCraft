package org.cyclops.evilcraft.block;

import net.minecraft.item.ItemBlock;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.item.ExcrementPileItemBlock;

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
    @ConfigurableProperty(category = ConfigurableTypeCategory.BLOCK, comment = "If Excrement can also poison any mob next to players.", isCommandable = true)
    public static boolean poisonEntities = false;
    
    /**
     * The relative effectiveness when compared to bonemeal if shift right click using.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.BLOCK, comment = "The relative effectiveness when compared to bonemeal if shift right click using.", isCommandable = true)
    public static int effectiveness = 3;

    /**
     * Make a new instance.
     */
    public ExcrementPileConfig() {
        super(
                EvilCraft._instance,
        	false,
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
