package org.cyclops.evilcraft.enchantment;

import org.cyclops.cyclopscore.config.extendedconfig.EnchantmentConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link EnchantmentPoisonTip}.
 * @author rubensworks
 *
 */
public class EnchantmentPoisonTipConfig extends EnchantmentConfig {

    public EnchantmentPoisonTipConfig() {
        super(
                EvilCraft._instance,
                "poison_tip",
                eConfig -> new EnchantmentPoisonTip()
        );
    }

}
