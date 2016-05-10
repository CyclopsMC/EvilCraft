package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.core.helper.obfuscation.ObfuscationHelpers;
import org.lwjgl.opengl.GL11;

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
	private static final int MAX_VIEW_DISTANCE = 30;
	
	private int scaleLife;
	private float originalScale;
	
	/**
	 * Make a new instance.
	 * @param world The world.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param z Z coordinate.
	 * @param scale The scale of this particle.
	 * @param xSpeed The X motion speed.
	 * @param ySpeed The Y motion speed.
	 * @param zSpeed The Z motion speed.
	 * @param red Red tint.
	 * @param green Green tint.
	 * @param blue Blue tint.
	 * @param ageMultiplier The multiplier of the maximum age (this will be multiplied with
	 * a partially random factor).
	 */
	public EntityBlurFX(World world, double x, double y, double z, float scale,
			double xSpeed, double ySpeed, double zSpeed,
			float red, float green, float blue, float ageMultiplier) {
		super(world, x, y, z, 0, 0, 0);
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.zSpeed = zSpeed;
		
		this.particleRed = red;
		this.particleGreen = green;
		this.particleBlue = blue;
		this.particleGravity = 0;
		
		this.particleScale *= scale;
		this.particleMaxAge = (int) ((rand.nextFloat() * 0.33F + 0.66F) * ageMultiplier);
		this.setSize(0.01F, 0.01F);
		
		this.prevPosX = posX;
		this.prevPosY = posY;
		this.prevPosZ = posZ;
		
		this.scaleLife = (int) (particleMaxAge / 2.5);
		this.originalScale = this.particleScale;
		
		validateDistance();
	}
	
	private void validateDistance() {
		EntityLivingBase renderentity = FMLClientHandler.instance().getClient().thePlayer;
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
    public void renderParticle(VertexBuffer worldRenderer, Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		float agescale = (float)particleAge / (float) scaleLife;
		if(agescale > 1F) {
			agescale = 2 - agescale;
		}

		particleScale = originalScale * agescale;

		int oldDrawMode = worldRenderer.getDrawMode();
		VertexFormat oldVertexFormat = worldRenderer.getVertexFormat();
        Tessellator.getInstance().draw();
		GlStateManager.pushMatrix();

		GlStateManager.depthMask(false);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

		Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);

		GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);

		float f10 = 0.5F * particleScale;
		float f11 = (float)(prevPosX + (posX - prevPosX) * f - interpPosX);
		float f12 = (float)(prevPosY + (posY - prevPosY) * f - interpPosY);
		float f13 = (float)(prevPosZ + (posZ - prevPosZ) * f - interpPosZ);

		worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
		int i = this.getBrightnessForRender(f5);
		int j = i >> 16 & 65535;
		int k = i & 65535;
        worldRenderer.pos(f11 - f1 * f10 - f4 * f10, f12 - f2 * f10, f13 - f3 * f10 - f5 * f10).tex(0, 1).
				color(particleRed, particleGreen, particleBlue, 0.9F).lightmap(j, k).endVertex();
        worldRenderer.pos(f11 - f1 * f10 + f4 * f10, f12 + f2 * f10, f13 - f3 * f10 + f5 * f10).tex(1, 1).
				color(particleRed, particleGreen, particleBlue, 0.9F).lightmap(j, k).endVertex();
        worldRenderer.pos(f11 + f1 * f10 + f4 * f10, f12 + f2 * f10, f13 + f3 * f10 + f5 * f10).tex(1, 0).
				color(particleRed, particleGreen, particleBlue, 0.9F).lightmap(j, k).endVertex();
        worldRenderer.pos(f11 + f1 * f10 - f4 * f10, f12 - f2 * f10, f13 + f3 * f10 - f5 * f10).tex(0, 0).
				color(particleRed, particleGreen, particleBlue, 0.9F).lightmap(j, k).endVertex();

        Tessellator.getInstance().draw();

		GlStateManager.disableBlend();
		GlStateManager.depthMask(true);

		GlStateManager.popMatrix();
		Minecraft.getMinecraft().renderEngine.bindTexture(ObfuscationHelpers.getParticleTexture());
		worldRenderer.begin(oldDrawMode, oldVertexFormat);
	}
	
	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		if(particleAge++ >= particleMaxAge) {
			setExpired();
		}

		ySpeed -= 0.04D * particleGravity;
		posX += xSpeed;
		posY += ySpeed;
		posZ += zSpeed;
		xSpeed *= 0.98000001907348633D;
		ySpeed *= 0.98000001907348633D;
		zSpeed *= 0.98000001907348633D;
	}

	/**
	 * Set the gravity for this particle.
	 * @param particleGravity The new gravity
	 */
	public void setGravity(float particleGravity) {
		this.particleGravity = particleGravity;
	}

}
