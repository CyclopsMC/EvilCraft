package org.cyclops.evilcraft.enchantment;

import org.cyclops.cyclopscore.config.extendedconfig.EnchantmentConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

/**
 * Config for {@link EnchantmentUnusing}.
 * @author rubensworks
 *
 */
public class EnchantmentUnusingConfig extends EnchantmentConfig {
    
    /**
     * The unique instance.
     */
    public static EnchantmentUnusingConfig _instance;

    /**
     * Make a new instance.
     */
    public EnchantmentUnusingConfig() {
        super(
                EvilCraft._instance,
            Reference.ENCHANTMENT_UNUSING,
            "unusing",
            null,
            EnchantmentUnusing.class
        );
    }
    
}
