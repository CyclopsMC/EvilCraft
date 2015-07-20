package org.cyclops.evilcraft.enchantment;

import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.EnchantmentConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

/**
 * Config for {@link EnchantmentLifeStealing}.
 * @author rubensworks
 *
 */
public class EnchantmentLifeStealingConfig extends EnchantmentConfig {
    
    /**
     * The unique instance.
     */
    public static EnchantmentLifeStealingConfig _instance;
    
    /**
     * The final modifier that should be applied to the healing amount.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ENCHANTMENT, comment = "The final modifier that should be applied to the healing amount.", isCommandable = true)
    public static double healModifier = 0.1D;

    /**
     * Make a new instance.
     */
    public EnchantmentLifeStealingConfig() {
        super(
                EvilCraft._instance,
            Reference.ENCHANTMENT_LIFESTEALING,
            "lifeStealing",
            null,
            EnchantmentLifeStealing.class
        );
    }
    
}
