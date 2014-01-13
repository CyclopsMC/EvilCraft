package evilcraft.enchantment;

import evilcraft.Reference;
import evilcraft.api.config.EnchantmentConfig;

public class EnchantmentBreakingConfig extends EnchantmentConfig {
    
    public static EnchantmentBreakingConfig _instance;

    public EnchantmentBreakingConfig() {
        super(
            Reference.ENCHANTMENT_BREAKING,
            "Breaking",
            "enchantmentBreaking",
            null,
            EnchantmentBreaking.class
        );
    }
    
}
