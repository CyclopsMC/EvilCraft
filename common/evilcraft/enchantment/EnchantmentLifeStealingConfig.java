package evilcraft.enchantment;

import evilcraft.Reference;
import evilcraft.api.config.EnchantmentConfig;

public class EnchantmentLifeStealingConfig extends EnchantmentConfig {
    
    public static EnchantmentLifeStealingConfig _instance;

    public EnchantmentLifeStealingConfig() {
        super(
            Reference.ENCHANTMENT_LIFESTEALING,
            "Life Stealing",
            "enchantmentLifestealing",
            null,
            EnchantmentLifeStealing.class
        );
    }
    
}
