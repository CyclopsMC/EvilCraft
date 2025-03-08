package org.cyclops.evilcraft.enchantment.component;

import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.common.NeoForge;
import org.cyclops.cyclopscore.config.extendedconfig.EnchantmentEffectComponentConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.enchantment.entityeffect.EnchantmentEntityEffectHealFromDamage;

/**
 * Config for {@link EnchantmentEntityEffectHealFromDamage}.
 * @author rubensworks
 *
 */
public class EnchantmentEffectComponentAmplifyDamageConfig extends EnchantmentEffectComponentConfigCommon<Integer, IModBase> {
    public EnchantmentEffectComponentAmplifyDamageConfig() {
        super(
                EvilCraft._instance,
                "amplify_damage",
                builder -> builder
                        .persistent(ExtraCodecs.POSITIVE_INT)
        );
        NeoForge.EVENT_BUS.register(new EnchantmentEffectComponentAmplifyDamage());
    }
}
