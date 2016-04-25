package org.cyclops.evilcraft.core.broom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.cyclops.evilcraft.api.broom.BroomModifier;
import org.cyclops.evilcraft.entity.item.EntityBroom;

/**
 * A potion effect that is applied on entity collision
 * @author rubensworks
 */
public class PotionEffectBroomCollision implements BroomModifier.ICollisionListener {

    private final Potion potion;
    private final int level;

    public PotionEffectBroomCollision(Potion potion, int level) {
        this.potion = potion;
        this.level = level;
    }

    public PotionEffectBroomCollision(Potion potion) {
        this(potion, 1);
    }

    @Override
    public void onCollide(EntityBroom broom, Entity entity, float modifierValue) {
        if (entity instanceof EntityLivingBase && modifierValue > 0) {
            ((EntityLivingBase) entity).addPotionEffect(
                    new PotionEffect(potion.id, (int) modifierValue, level));
        }
    }
}
