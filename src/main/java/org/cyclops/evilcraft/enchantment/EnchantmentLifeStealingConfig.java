package org.cyclops.evilcraft.enchantment;

import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.EnchantmentConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link EnchantmentLifeStealing}.
 * @author rubensworks
 *
 */
public class EnchantmentLifeStealingConfig extends EnchantmentConfig {

    @ConfigurableProperty(category = "enchantment", comment = "The final modifier that should be applied to the healing amount.", isCommandable = true)
    public static double healModifier = 0.1D;

    public EnchantmentLifeStealingConfig() {
        super(
                EvilCraft._instance,
            "life_stealing",
            eConfig -> new EnchantmentLifeStealing()
        );
    }

}
