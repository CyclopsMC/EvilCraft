package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.cyclops.cyclopscore.client.particle.ParticleBlur;

import javax.annotation.Nullable;

/**
 * A blurred static fading particle with any possible color targetted at a certain entity.
 * @author rubensworks
 *
 */
public class ParticleBlurTargettedEntity extends ParticleBlur {

    @Nullable
    private final LivingEntity entity;

    public ParticleBlurTargettedEntity(ParticleBlurTargettedEntityData data, ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(data, world, x, y, z, motionX, motionY, motionZ);
        Entity entityUnknown = world.getEntity(data.getEntityId());
        this.entity = entityUnknown != null ? (LivingEntity) entityUnknown : null;
    }

    @Override
    public void tick() {
        xo = x;
        yo = y;
        zo = z;

        if(age++ >= lifetime || entity == null || entity.getUseItemRemainingTicks() == 0) {
            remove();
        }

        float f = (float)this.age / (float)this.lifetime;
        float f1 = f;
        f = -f + f * f * 2.0F;
        //f = 1.0F - f;
        yd -= 0.04D * gravity;
        if (entity != null) {
            x = entity.getX() + xd * f;
            y = entity.getY() + entity.getEyeHeight() - 0.5F + yd * f + (double) (1.0F - f1) + (Minecraft.getInstance().player == entity ? 0 : 1);
            z = entity.getZ() + zd * f;
        }
    }

}
