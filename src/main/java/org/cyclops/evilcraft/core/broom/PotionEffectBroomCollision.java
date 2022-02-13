package org.cyclops.evilcraft.core.broom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import org.cyclops.evilcraft.api.broom.BroomModifier;
import org.cyclops.evilcraft.entity.item.EntityBroom;

/**
 * A potion effect that is applied on entity collision
 * @author rubensworks
 */
public class PotionEffectBroomCollision implements BroomModifier.ICollisionListener {

    private final Effect potion;
    private final int level;

    public PotionEffectBroomCollision(Effect potion, int level) {
        this.potion = potion;
        this.level = level;
    }

    public PotionEffectBroomCollision(Effect potion) {
        this(potion, 1);
    }

    @Override
    public void onCollide(EntityBroom broom, Entity entity, float modifierValue) {
        if (entity instanceof LivingEntity && modifierValue > 0) {
            ((LivingEntity) entity).addEffect(
                    new EffectInstance(potion, (int) modifierValue, level));
        }
    }
}
