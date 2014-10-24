package evilcraft.enchantment;

import evilcraft.Reference;
import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.extendedconfig.EnchantmentConfig;

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
            Reference.ENCHANTMENT_LIFESTEALING,
            "lifeStealing",
            null,
            EnchantmentLifeStealing.class
        );
    }
    
}
