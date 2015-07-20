package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Ender Tear.
 * @author rubensworks
 *
 */
public class EnderTearConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static EnderTearConfig _instance;

    /**
     * The 1/X chance on dropping this item.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "The 1/X chance on dropping this item.", isCommandable = true)
    public static int chanceDrop = 10;

    /**
     * The amount of liquid ender produced when TE or TCon is available.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "The amount of liquid ender produced when TE or TCon is available.", requiresMcRestart = true)
    public static int mbLiquidEnder = 2000;

    /**
     * Make a new instance.
     */
    public EnderTearConfig() {
        super(
                EvilCraft._instance,
            true,
            "enderTear",
            null,
            null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        return(ConfigurableItem) new ConfigurableItem(this).setMaxStackSize(16);
    }
    
}
