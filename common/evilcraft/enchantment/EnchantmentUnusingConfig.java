package evilcraft.enchantment;

import evilcraft.Reference;
import evilcraft.api.config.EnchantmentConfig;

public class EnchantmentUnusingConfig extends EnchantmentConfig {
    
    public static EnchantmentUnusingConfig _instance;

    public EnchantmentUnusingConfig() {
        super(
            Reference.ENCHANTMENT_UNUSING,
            "Unusing",
            "enchantmentUnusing",
            null,
            EnchantmentUnusing.class
        );
    }
    
}
