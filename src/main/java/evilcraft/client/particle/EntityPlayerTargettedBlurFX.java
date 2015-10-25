package evilcraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * A blurred static fading particle with any possible color targetted at a certain player.
 * @author rubensworks
 *
 */
public class EntityPlayerTargettedBlurFX extends EntityBlurFX {

	private final EntityPlayer player;

	/**
	 * Make a new instance.
	 * @param world The world.
	 * @param scale The scale of this particle.
	 * @param motionX The X motion speed.
	 * @param motionY The Y motion speed.
	 * @param motionZ The Z motion speed.
	 * @param red Red tint.
	 * @param green Green tint.
	 * @param blue Blue tint.
	 * @param ageMultiplier The multiplier of the maximum age (this will be multiplied with
	 * @param player The player to target
	 * a partially random factor).
	 */
	public EntityPlayerTargettedBlurFX(World world, float scale,
									   double motionX, double motionY, double motionZ,
									   float red, float green, float blue, float ageMultiplier,
									   EntityPlayer player) {
		super(world, player.posX, player.posY, player.posZ, scale, motionX, motionY, motionZ, red, green, blue, ageMultiplier);
		this.player = player;
	}
	
	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		if(particleAge++ >= particleMaxAge || !player.isUsingItem()) {
			setDead();
		}

		float f = (float)this.particleAge / (float)this.particleMaxAge;
		float f1 = f;
		f = -f + f * f * 2.0F;
		//f = 1.0F - f;
		motionY -= 0.04D * particleGravity;
		posX = player.posX + motionX * f;
		posY = player.posY - 0.5F + motionY * f + (double)(1.0F - f1) + (Minecraft.getMinecraft().thePlayer == player ? 0 : 1);
		posZ = player.posZ + motionZ * f;
	}

}
