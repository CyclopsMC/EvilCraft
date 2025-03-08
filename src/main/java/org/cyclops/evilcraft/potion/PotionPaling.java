package org.cyclops.evilcraft.potion;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.evilcraft.ExtendedDamageSources;

/**
 * Potion effect for letting entities fade away and leaving a portal behind in their place.
 * @author rubensworks
 *
 */
public class PotionPaling extends MobEffect {

    public PotionPaling() {
        super(MobEffectCategory.HARMFUL, IModHelpers.get().getBaseHelpers().RGBToInt(56, 25, 97));
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        entity.hurt(ExtendedDamageSources.paling(entity.level()), ((float) amplifier) / 4);
        return super.applyEffectTick(level, entity, amplifier);
    }
}
