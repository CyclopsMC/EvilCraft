package org.cyclops.evilcraft.enchantment.entityeffect;

import org.cyclops.cyclopscore.config.extendedconfig.EnchantmentEntityEffectConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link EnchantmentEntityEffectHealFromDamage}.
 * @author rubensworks
 *
 */
public class EnchantmentEntityEffectSummonVengeanceSpiritConfig extends EnchantmentEntityEffectConfig {
    public EnchantmentEntityEffectSummonVengeanceSpiritConfig() {
        super(
                EvilCraft._instance,
                "summon_vengeance_spirit",
                eConfig -> EnchantmentEntityEffectSummonVengeanceSpirit.CODEC
        );
    }
}
