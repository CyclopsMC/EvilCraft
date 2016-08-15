package org.cyclops.evilcraft.enchantment;

import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.EnchantmentConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

/**
 * Config for {@link EnchantmentVengeance}.
 * @author rubensworks
 *
 */
public class EnchantmentVengeanceConfig extends EnchantmentConfig {

    /**
     * The unique instance.
     */
    public static EnchantmentVengeanceConfig _instance;

    /**
     * The area of effect in blocks in which this tool could enable vengeance spirits.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "The area of effect in blocks in which this tool could enable vengeance spirits.", isCommandable = true)
    public static int areaOfEffect = 5;

    /**
     * The ^-1 chance for which vengeance spirits could be toggled.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "The ^-1 chance for which vengeance spirits could be toggled.", isCommandable = true)
    public static int vengeanceChance = 3;

    /**
     * Make a new instance.
     */
    public EnchantmentVengeanceConfig() {
        super(
                EvilCraft._instance,
            Reference.ENCHANTMENT_VENGEANCE,
            "vengeance",
            null,
            EnchantmentVengeance.class
        );
    }
    
}
