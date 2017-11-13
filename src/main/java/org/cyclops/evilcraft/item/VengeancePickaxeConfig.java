package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

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
     * The default fortune enchantment level on these Vengeance Pickaxes.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "The default fortune enchantment level on these pickaxes.", requiresMcRestart = true)
    public static int fortuneLevel = 5;

    /**
     * The default vengeance enchantment level on these Vengeance Pickaxes.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "The default vengeance enchantment level on these pickaxes.", requiresMcRestart = true)
    public static int vengeanceLevel = 3;

    /**
     * Make a new instance.
     */
    public VengeancePickaxeConfig() {
        super(
                EvilCraft._instance,
        	true,
            "vengeance_pickaxe",
            null,
            VengeancePickaxe.class
        );
    }
    
}
