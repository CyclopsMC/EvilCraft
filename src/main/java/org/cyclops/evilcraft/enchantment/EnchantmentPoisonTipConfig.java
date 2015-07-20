package org.cyclops.evilcraft.enchantment;

import org.cyclops.cyclopscore.config.extendedconfig.EnchantmentConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

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
                EvilCraft._instance,
            Reference.ENCHANTMENT_POISON_TIP,
            "poisonTip",
            null,
            EnchantmentPoisonTip.class
        );
    }
    
}
