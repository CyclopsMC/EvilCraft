package org.cyclops.evilcraft.client.particle;

import net.minecraft.world.World;

/**
 * A blurred static fading particle with any possible color targetted at a certain location.
 * @author rubensworks
 *
 */
public class EntityTargettedBlurFX extends EntityBlurFX {

	private final double targetX;
	private final double targetY;
	private final double targetZ;

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
	 * @param targetX The target X
	 * @param targetY The target Y
	 * @param targetZ The target Z
	 * a partially random factor).
	 */
	public EntityTargettedBlurFX(World world, float scale,
								 double xSpeed, double ySpeed, double zSpeed,
								 float red, float green, float blue, float ageMultiplier,
								 double targetX, double targetY, double targetZ) {
		super(world, targetX, targetY, targetZ, scale, xSpeed, ySpeed, zSpeed, red, green, blue, ageMultiplier);
		this.targetX = targetX;
		this.targetY = targetY;
		this.targetZ = targetZ;
	}
	
	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		if(particleAge++ >= particleMaxAge) {
			setExpired();
		}

		float f = (float)this.particleAge / (float)this.particleMaxAge;
		float f1 = f;
		f = -f + f * f * 2.0F;
		f = 1.0F - f;
		ySpeed -= 0.04D * particleGravity;
		posX = targetX + xSpeed * f;
		posY = targetY + ySpeed * f + (double)(1.0F - f1);
		posZ = targetZ + zSpeed * f;
	}

}
