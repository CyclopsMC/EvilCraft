package evilcraft.item;

import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Config for the {@link evilcraft.item.EnderTear}.
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
            true,
            "enderTear",
            null,
            EnderTear.class
        );
    }
    
}
