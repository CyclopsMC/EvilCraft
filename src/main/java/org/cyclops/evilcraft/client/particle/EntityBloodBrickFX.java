package org.cyclops.evilcraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.core.helper.obfuscation.ObfuscationHelpers;
import org.lwjgl.opengl.GL11;

/**
 * A blurred static fading particle with any possible color.
 * Based on Vazkii's FXWisp's from Botania.
 * @author rubensworks
 *
 */
public class EntityBloodBrickFX extends EntityFX {
	
	private static final ResourceLocation TEXTURE = new ResourceLocation(
			Reference.MOD_ID, Reference.TEXTURE_PATH_PARTICLES + "bloodBrickActivation.png");
	private static final ResourceLocation TEXTURE_FLIPPED = new ResourceLocation(
			Reference.MOD_ID, Reference.TEXTURE_PATH_PARTICLES + "bloodBrickActivation_flipped.png");
	private static final int TESSELATOR_BRIGHTNESS = 160;
	
	private EnumFacing side;
	
	/**
	 * Make a new instance.
	 * @param world The world.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param z Z coordinate.
	 * @param side The side of the blockState to draw this particle on.
	 */
	public EntityBloodBrickFX(World world, double x, double y, double z, EnumFacing side) {
		super(world, x, y, z, 0, 0, 0);
		this.side = side;
		
		this.xSpeed = 0;
		this.ySpeed = 0;
		this.zSpeed = 0;
		
		this.particleRed = 1;
		this.particleGreen = 1;
		this.particleBlue = 1;
		this.particleGravity = 0;
		
		//this.particleScale *= scale;
		//this.particleMaxAge = (int) ((rand.nextFloat() * 0.33F + 0.66F) * ageMultiplier);
		this.particleMaxAge = 40;
		this.setSize(0.01F, 0.01F);
		
		this.posX += (double) (side.getFrontOffsetX()) / 30D;
		this.posY += (double) (side.getFrontOffsetY()) / 30D;
		this.posZ += (double) (side.getFrontOffsetZ()) / 30D;
		
		this.prevPosX = posX;
		this.prevPosY = posY;
		this.prevPosZ = posZ;
	}

	@Override
	public void renderParticle(VertexBuffer worldRenderer, Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		int oldDrawMode = worldRenderer.getDrawMode();
		VertexFormat oldVertexFormat = worldRenderer.getVertexFormat();
		Tessellator.getInstance().draw();
		GlStateManager.pushMatrix();

        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
		
		// Flipping to make sure the particle lines match the blockState lines.
		boolean flip = side == EnumFacing.DOWN;
		Minecraft.getMinecraft().renderEngine.bindTexture(flip ? TEXTURE_FLIPPED : TEXTURE);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);

		float f11 = (float)(prevPosX + (posX - prevPosX) * f - interpPosX);
		float f12 = (float)(prevPosY + (posY - prevPosY) * f - interpPosY);
		float f13 = (float)(prevPosZ + (posZ - prevPosZ) * f - interpPosZ);
        GlStateManager.translate(f11 + 0.5D, f12 + 0.5D, f13 + 0.5D);
		
		// Several rotations to make sure the particle lines match the blockState lines.
		if(side == EnumFacing.WEST) { // X-
            GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
	    }
		if(side == EnumFacing.NORTH) { // Z-
            GlStateManager.rotate(90.0F, 0.0F, 0.0F, -1.0F);
		}
		if(side == EnumFacing.EAST) { // X+
            GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
	    }
		if(side == EnumFacing.SOUTH) { // Z+
            GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
		}
		if(side == EnumFacing.DOWN) { // Y-
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
		}

        GlStateManager.rotate(90.0F, side.getFrontOffsetY(), -side.getFrontOffsetX(), side.getFrontOffsetZ());
		
	    if(side.getFrontOffsetZ() > 0) {
            GlStateManager.translate(0.0D, 0.0D, 0.5D);
            GlStateManager.rotate(180.0F, 0.0F, -1.0F, 0.0F);
	    } else {
            GlStateManager.translate(0.0D, 0.0D, -0.5D);
	    }

		float brightness = (float) particleAge / (float) (particleMaxAge / 2);
		if(brightness > 1) brightness = (float) (particleMaxAge - particleAge) / 2 / (float) (particleMaxAge / 2);

		float offsetter = 1.0F;
		int i = TESSELATOR_BRIGHTNESS;
		int j = i >> 16 & 65535;
		int k = i & 65535;
		worldRenderer.pos(-0.5D * offsetter, 0.5D * offsetter, 0.0D).tex(0.0D, 1.0D)
				.color(particleRed, particleGreen, particleBlue, brightness).lightmap(j, k).endVertex();
        worldRenderer.pos(0.5D * offsetter, 0.5D * offsetter, 0.0D).tex(1.0D, 1.0D)
				.color(particleRed, particleGreen, particleBlue, brightness).lightmap(j, k).endVertex();
        worldRenderer.pos(0.5D * offsetter, -0.5D * offsetter, 0.0D).tex(1.0D, 0.0D)
				.color(particleRed, particleGreen, particleBlue, brightness).lightmap(j, k).endVertex();
        worldRenderer.pos(-0.5D * offsetter, -0.5D * offsetter, 0.0D).tex(0.0D, 0.0D)
				.color(particleRed, particleGreen, particleBlue, brightness).lightmap(j, k).endVertex();
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
		xSpeed *= 0.95D;
		ySpeed *= 0.95D;
		zSpeed *= 0.95D;
	}

	/**
	 * Set the gravity for this particle.
	 * @param particleGravity The new gravity
	 */
	public void setGravity(float particleGravity) {
		this.particleGravity = particleGravity;
	}

}
