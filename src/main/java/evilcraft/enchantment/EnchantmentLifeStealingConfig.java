package evilcraft.enchantment;

import evilcraft.Reference;
import evilcraft.api.config.EnchantmentConfig;

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
