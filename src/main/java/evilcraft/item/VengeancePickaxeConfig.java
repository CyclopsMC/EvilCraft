package evilcraft.item;

import evilcraft.core.config.ElementTypeCategory;
import evilcraft.core.config.ItemConfig;
import evilcraft.core.config.configurable.ConfigurableProperty;

/**
 * Config for the {@link VengeancePickaxe}.
 * @author rubensworks
 *
 */
public class VengeancePickaxeConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static VengeancePickaxeConfig _instance;
    
    /**
     * The area of effect in blocks in which this tool could enable vengeance spirits.
     */
    @ConfigurableProperty(category = ElementTypeCategory.GENERAL, comment = "The area of effect in blocks in which this tool could enable vengeance spirits.")
    public static int areaOfEffect = 5;
    
    /**
     * The ^-1 chance for which vengeance spirits could be toggled.
     */
    @ConfigurableProperty(category = ElementTypeCategory.GENERAL, comment = "The ^-1 chance for which vengeance spirits could be toggled.")
    public static int vengeanceChance = 1;

    /**
     * Make a new instance.
     */
    public VengeancePickaxeConfig() {
        super(
        	true,
            "vengeancePickaxe",
            null,
            VengeancePickaxe.class
        );
    }
    
}
