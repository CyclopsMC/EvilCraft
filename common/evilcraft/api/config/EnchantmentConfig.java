package evilcraft.api.config;

import net.minecraft.enchantment.Enchantment;

/**
 * Config for enchantments.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class EnchantmentConfig extends ExtendedConfig<EnchantmentConfig>{

    /**
     * Make a new instance.
     * @param defaultId The default ID for the configurable.
     * @param name The name for the configurable.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param element The class of this configurable.
     */
    public EnchantmentConfig(int defaultId, String name, String namedId,
            String comment, Class<? extends Enchantment> element) {
        super(defaultId, name, namedId, comment, element);
    }

}
