package evilcraft.enchantment;

import evilcraft.Reference;
import evilcraft.api.config.EnchantmentConfig;

/**
 * Config for {@link EnchantmentPoisonTip}.
 * @author rubensworks
 *
 */
public class EnchantmentPoisonTipConfig extends EnchantmentConfig {
    
    /**
     * The unique instance.
     */
    public static EnchantmentPoisonTipConfig _instance;

    /**
     * Make a new instance.
     */
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
