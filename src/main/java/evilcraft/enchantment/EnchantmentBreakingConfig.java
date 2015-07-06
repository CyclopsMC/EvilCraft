package evilcraft.enchantment;

import evilcraft.EvilCraft;
import evilcraft.Reference;
import org.cyclops.cyclopscore.config.extendedconfig.EnchantmentConfig;

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
