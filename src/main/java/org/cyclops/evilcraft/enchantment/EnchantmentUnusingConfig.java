package org.cyclops.evilcraft.enchantment;

import org.cyclops.cyclopscore.config.extendedconfig.EnchantmentConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link EnchantmentUnusing}.
 * @author rubensworks
 *
 */
public class EnchantmentUnusingConfig extends EnchantmentConfig {

    public EnchantmentUnusingConfig() {
        super(
                EvilCraft._instance,
                "unusing",
                eConfig -> new EnchantmentUnusing()
        );
    }

}
