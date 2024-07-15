package org.cyclops.evilcraft.enchantment.entityeffect;

import org.cyclops.cyclopscore.config.extendedconfig.EnchantmentEntityEffectConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link EnchantmentEntityEffectHealFromDamage}.
 * @author rubensworks
 *
 */
public class EnchantmentEntityEffectHealFromDamageConfig extends EnchantmentEntityEffectConfig {
    public EnchantmentEntityEffectHealFromDamageConfig() {
        super(
                EvilCraft._instance,
                "heal_from_damage",
                eConfig -> EnchantmentEntityEffectHealFromDamage.CODEC
        );
    }
}
