package evilcraft.enchantment;

import evilcraft.Reference;
import evilcraft.core.config.extendedconfig.EnchantmentConfig;

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
            Reference.ENCHANTMENT_UNUSING,
            "unusing",
            null,
            EnchantmentUnusing.class
        );
    }
    
}
