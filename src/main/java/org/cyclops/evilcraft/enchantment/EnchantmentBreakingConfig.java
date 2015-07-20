package org.cyclops.evilcraft.enchantment;

import org.cyclops.cyclopscore.config.extendedconfig.EnchantmentConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

/**
 * Config for {@link EnchantmentBreaking}.
 * @author rubensworks
 *
 */
public class EnchantmentBreakingConfig extends EnchantmentConfig {
    
    /**
     * The unique instance.
     */
    public static EnchantmentBreakingConfig _instance;

    /**
     * Make a new instance.
     */
    public EnchantmentBreakingConfig() {
        super(
                EvilCraft._instance,
            Reference.ENCHANTMENT_BREAKING,
            "breaking",
            null,
            EnchantmentBreaking.class
        );
    }
    
}
