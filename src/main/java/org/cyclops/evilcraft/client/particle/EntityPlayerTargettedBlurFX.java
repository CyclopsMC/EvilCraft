package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

/**
 * A blurred static fading particle with any possible color targetted at a certain entity.
 * @author rubensworks
 *
 */
public class EntityPlayerTargettedBlurFX extends EntityBlurFX {

	private final EntityLivingBase entity;

	/**
	 * Make a new instance.
	 * @param world The world.
	 * @param scale The scale of this particle.
	 * @param xSpeed The X motion speed.
	 * @param ySpeed The Y motion speed.
	 * @param zSpeed The Z motion speed.
	 * @param red Red tint.
	 * @param green Green tint.
	 * @param blue Blue tint.
	 * @param ageMultiplier The multiplier of the maximum age (this will be multiplied with
	 * @param entity The entity to target
	 * a partially random factor).
	 */
	public EntityPlayerTargettedBlurFX(World world, float scale,
									   double xSpeed, double ySpeed, double zSpeed,
									   float red, float green, float blue, float ageMultiplier,
									   EntityLivingBase entity) {
		super(world, entity.posX, entity.posY, entity.posZ, scale, xSpeed, ySpeed, zSpeed, red, green, blue, ageMultiplier);
		this.entity = entity;
	}
	
	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		if(particleAge++ >= particleMaxAge || entity.getItemInUseCount() == 0) {
			setExpired();
		}

		float f = (float)this.particleAge / (float)this.particleMaxAge;
		float f1 = f;
		f = -f + f * f * 2.0F;
		//f = 1.0F - f;
		ySpeed -= 0.04D * particleGravity;
		posX = entity.posX + xSpeed * f;
		posY = entity.posY + entity.getEyeHeight() - 0.5F + ySpeed * f + (double)(1.0F - f1) + (Minecraft.getMinecraft().thePlayer == entity ? 0 : 1);
		posZ = entity.posZ + zSpeed * f;
	}

}
