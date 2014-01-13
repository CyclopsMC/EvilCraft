package evilcraft.api.config;

import net.minecraft.enchantment.Enchantment;

public abstract class EnchantmentConfig extends ExtendedConfig<EnchantmentConfig>{

    public EnchantmentConfig(int defaultId, String name, String namedId,
            String comment, Class<? extends Enchantment> element) {
        super(defaultId, name, namedId, comment, element);
    }

}
