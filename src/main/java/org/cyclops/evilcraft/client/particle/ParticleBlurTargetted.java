package org.cyclops.evilcraft.client.particle;

import net.minecraft.world.World;
import org.cyclops.cyclopscore.client.particle.ParticleBlur;

/**
 * A blurred static fading particle with any possible color targetted at a certain location.
 * @author rubensworks
 *
 */
public class ParticleBlurTargetted extends ParticleBlur {

	private final double targetX;
	private final double targetY;
	private final double targetZ;

	public ParticleBlurTargetted(ParticleBlurTargettedData data, World world, double x, double y, double z, double motionX, double motionY, double motionZ) {
		super(data, world, x, y, z, motionX, motionY, motionZ);
		this.targetX = data.getTargetX();
		this.targetY = data.getTargetY();
		this.targetZ = data.getTargetZ();
	}
	
	@Override
	public void tick() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		if(age++ >= maxAge) {
			setExpired();
		}

		float f = (float)this.age / (float)this.maxAge;
		float f1 = f;
		f = -f + f * f * 2.0F;
		f = 1.0F - f;
		motionY -= 0.04D * particleGravity;
		posX = targetX + motionX * f;
		posY = targetY + motionY * f + (double)(1.0F - f1);
		posZ = targetZ + motionZ * f;
	}

}
