package org.cyclops.evilcraft.enchantment;

import org.cyclops.cyclopscore.config.extendedconfig.EnchantmentConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link EnchantmentBreaking}.
 * @author rubensworks
 *
 */
public class EnchantmentBreakingConfig extends EnchantmentConfig {

    public EnchantmentBreakingConfig() {
        super(
                EvilCraft._instance,
                "breaking",
                eConfig -> new EnchantmentBreaking()
        );
    }

}
