package org.cyclops.evilcraft.enchantment.entityeffect;

import org.cyclops.cyclopscore.config.extendedconfig.EnchantmentEntityEffectConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link EnchantmentEntityEffectHealFromDamage}.
 * @author rubensworks
 *
 */
public class EnchantmentEntityEffectSummonVengeanceSpiritConfig extends EnchantmentEntityEffectConfigCommon<IModBase> {
    public EnchantmentEntityEffectSummonVengeanceSpiritConfig() {
        super(
                EvilCraft._instance,
                "summon_vengeance_spirit",
                eConfig -> EnchantmentEntityEffectSummonVengeanceSpirit.CODEC
        );
    }
}
