package org.cyclops.evilcraft.enchantment.entityeffect;

import org.cyclops.cyclopscore.config.extendedconfig.EnchantmentEntityEffectConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link EnchantmentEntityEffectHealFromDamage}.
 * @author rubensworks
 *
 */
public class EnchantmentEntityEffectHealFromDamageConfig extends EnchantmentEntityEffectConfigCommon<IModBase> {
    public EnchantmentEntityEffectHealFromDamageConfig() {
        super(
                EvilCraft._instance,
                "heal_from_damage",
                eConfig -> EnchantmentEntityEffectHealFromDamage.CODEC
        );
    }
}
