package org.cyclops.evilcraft.core.broom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.cyclops.evilcraft.api.broom.BroomModifier;
import org.cyclops.evilcraft.entity.item.EntityBroom;

/**
 * A potion effect that is applied on entity collision
 * @author rubensworks
 */
public class PotionEffectBroomCollision implements BroomModifier.ICollisionListener {

    private final MobEffect potion;
    private final int level;

    public PotionEffectBroomCollision(MobEffect potion, int level) {
        this.potion = potion;
        this.level = level;
    }

    public PotionEffectBroomCollision(MobEffect potion) {
        this(potion, 1);
    }

    @Override
    public void onCollide(EntityBroom broom, Entity entity, float modifierValue) {
        if (entity instanceof LivingEntity && modifierValue > 0) {
            ((LivingEntity) entity).addEffect(
                    new MobEffectInstance(potion, (int) modifierValue, level));
        }
    }
}
