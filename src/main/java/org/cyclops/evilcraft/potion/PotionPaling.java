package org.cyclops.evilcraft.potion;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.evilcraft.ExtendedDamageSource;

/**
 * Potion effect for letting entities fade away and leaving a portal behind in their place.
 * @author rubensworks
 *
 */
public class PotionPaling extends Effect {

    public PotionPaling() {
        super(EffectType.HARMFUL, Helpers.RGBToInt(56, 25, 97));
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        entity.hurt(ExtendedDamageSource.paling, ((float) amplifier) / 4);
    }
}
