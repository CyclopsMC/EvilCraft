package org.cyclops.evilcraft.potion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.evilcraft.ExtendedDamageSources;

/**
 * Potion effect for letting entities fade away and leaving a portal behind in their place.
 * @author rubensworks
 *
 */
public class PotionPaling extends MobEffect {

    public PotionPaling() {
        super(MobEffectCategory.HARMFUL, Helpers.RGBToInt(56, 25, 97));
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        entity.hurt(ExtendedDamageSources.paling(entity.level()), ((float) amplifier) / 4);
    }
}
