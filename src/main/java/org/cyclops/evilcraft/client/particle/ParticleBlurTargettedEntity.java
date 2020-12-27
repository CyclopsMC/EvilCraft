package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
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

	public ParticleBlurTargettedEntity(ParticleBlurTargettedEntityData data, ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ) {
		super(data, world, x, y, z, motionX, motionY, motionZ);
		Entity entityUnknown = world.getEntityByID(data.getEntityId());
		this.entity = entityUnknown != null ? (LivingEntity) entityUnknown : null;
	}
	
	@Override
	public void tick() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		if(age++ >= maxAge || entity == null || entity.getItemInUseCount() == 0) {
			setExpired();
		}

		float f = (float)this.age / (float)this.maxAge;
		float f1 = f;
		f = -f + f * f * 2.0F;
		//f = 1.0F - f;
		motionY -= 0.04D * particleGravity;
		if (entity != null) {
			posX = entity.getPosX() + motionX * f;
			posY = entity.getPosY() + entity.getEyeHeight() - 0.5F + motionY * f + (double) (1.0F - f1) + (Minecraft.getInstance().player == entity ? 0 : 1);
			posZ = entity.getPosZ() + motionZ * f;
		}
	}

}
