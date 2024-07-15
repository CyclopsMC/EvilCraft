package org.cyclops.evilcraft.enchantment.component;

import com.mojang.serialization.Codec;
import net.neoforged.neoforge.common.NeoForge;
import org.cyclops.cyclopscore.config.extendedconfig.EnchantmentEffectComponentConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link EnchantmentEffectComponentStopDamage}.
 * @author rubensworks
 *
 */
public class EnchantmentEffectComponentStopDamageConfig extends EnchantmentEffectComponentConfig<Boolean> {
    public EnchantmentEffectComponentStopDamageConfig() {
        super(
                EvilCraft._instance,
                "stop_damage",
                builder -> builder
                        .persistent(Codec.BOOL)
        );
        NeoForge.EVENT_BUS.register(new EnchantmentEffectComponentStopDamage());
    }
}
