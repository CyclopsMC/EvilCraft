package evilcraft.enchantment;

import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig;

public class EnchantmentPoisonTipConfig extends ExtendedConfig {
    
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
