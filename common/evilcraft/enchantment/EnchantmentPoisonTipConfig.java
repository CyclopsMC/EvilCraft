package evilcraft.enchantment;

import evilcraft.Reference;
import evilcraft.api.config.EnchantmentConfig;

public class EnchantmentPoisonTipConfig extends EnchantmentConfig {
    
    public static EnchantmentPoisonTipConfig _instance;

    public EnchantmentPoisonTipConfig() {
        super(
            Reference.ENCHANTMENT_POISON_TIP,
            "Poison Tip",
            "enchantmentPoisonTip",
            null,
            EnchantmentPoisonTip.class
        );
    }
    
}
