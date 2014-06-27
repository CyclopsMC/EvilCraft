package evilcraft.render.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import evilcraft.Reference;
import evilcraft.api.obfuscation.ObfuscationHelper;

/**
 * A blurred static fading particle with any possible color.
 * Based on Vazkii's FXWisp's from Botania.
 * @author rubensworks
 *
 */
public class EntityBlurFX extends EntityFX {
	
	private static final ResourceLocation TEXTURE = new ResourceLocation(
			Reference.MOD_ID, Reference.TEXTURE_PATH_PARTICLES + "particleBlur.png");
	private static final int TESSELATOR_BRIGHTNESS = 240;
	private static final int MAX_VIEW_DISTANCE = 100;
	
	private int scaleLife;
	private float originalScale;
	
	/**
	 * Make a new instance.
	 * @param world The world.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param z Z coordinate.
	 * @param scale The scale of this particle.
	 * @param motionX The X motion speed.
	 * @param motionY The Y motion speed.
	 * @param motionZ The Z motion speed.
	 * @param red Red tint.
	 * @param green Green tint.
	 * @param blue Blue tint.
	 * @param ageMultiplier The multiplier of the maximum age (this will be multiplied with
	 * a partially random factor).
	 */
	public EntityBlurFX(World world, double x, double y, double z, float scale,
			double motionX, double motionY, double motionZ, 
			float red, float green, float blue, float ageMultiplier) {
		super(world, x, y, z, 0, 0, 0);
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
		
		this.particleRed = red;
		this.particleGreen = green;
		this.particleBlue = blue;
		this.particleGravity = 0;
		
		this.particleScale *= scale;
		this.particleMaxAge = (int) ((rand.nextFloat() * 0.33F + 0.66F) * ageMultiplier);
		this.noClip = true;
		this.setSize(0.01F, 0.01F);
		
		this.prevPosX = posX;
		this.prevPosY = posY;
		this.prevPosZ = posZ;
		
		this.scaleLife = (int) (particleMaxAge / 2.5);
		this.originalScale = this.particleScale;
		
		validateDistance();
	}
	
	private void validateDistance() {
		EntityLivingBase renderentity = FMLClientHandler.instance().getClient().renderViewEntity;
		int visibleDistance = MAX_VIEW_DISTANCE;
		
		if(!FMLClientHandler.instance().getClient().gameSettings.fancyGraphics) {
			visibleDistance = visibleDistance / 2;
		}

		if(renderentity == null
				|| renderentity.getDistance(posX, posY, posZ) > visibleDistance) {
			particleMaxAge = 0;
		}
	}

	@Override
	public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
		float agescale = 0;
		agescale = (float)particleAge / (float) scaleLife;
		if(agescale > 1F) {
			agescale = 2 - agescale;
		}

		particleScale = originalScale * agescale;

		tessellator.draw();
		GL11.glPushMatrix();

		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

		Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);

		float f10 = 0.5F * particleScale;
		float f11 = (float)(prevPosX + (posX - prevPosX) * f - interpPosX);
		float f12 = (float)(prevPosY + (posY - prevPosY) * f - interpPosY);
		float f13 = (float)(prevPosZ + (posZ - prevPosZ) * f - interpPosZ);

		tessellator.startDrawingQuads();
		tessellator.setBrightness(TESSELATOR_BRIGHTNESS);
		tessellator.setColorRGBA_F(particleRed, particleGreen, particleBlue, 0.9F);
		tessellator.addVertexWithUV(f11 - f1 * f10 - f4 * f10, f12 - f2 * f10, f13 - f3 * f10 - f5 * f10, 0, 1);
		tessellator.addVertexWithUV(f11 - f1 * f10 + f4 * f10, f12 + f2 * f10, f13 - f3 * f10 + f5 * f10, 1, 1);
		tessellator.addVertexWithUV(f11 + f1 * f10 + f4 * f10, f12 + f2 * f10, f13 + f3 * f10 + f5 * f10, 1, 0);
		tessellator.addVertexWithUV(f11 + f1 * f10 - f4 * f10, f12 - f2 * f10, f13 + f3 * f10 - f5 * f10, 0, 0);

		tessellator.draw();

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);

		GL11.glPopMatrix();
		Minecraft.getMinecraft().renderEngine.bindTexture(ObfuscationHelper.getParticleTexture());
		tessellator.startDrawingQuads();
	}
	
	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		if(particleAge++ >= particleMaxAge) {
			setDead();
		}

		motionY -= 0.04D * particleGravity;
		posX += motionX;
		posY += motionY;
		posZ += motionZ;
		motionX *= 0.98000001907348633D;
		motionY *= 0.98000001907348633D;
		motionZ *= 0.98000001907348633D;
	}

	/**
	 * Set the gravity for this particle.
	 * @param particleGravity The new gravity
	 */
	public void setGravity(float particleGravity) {
		this.particleGravity = particleGravity;
	}

}
