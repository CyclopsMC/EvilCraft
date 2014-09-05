package evilcraft.render.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import evilcraft.Reference;
import evilcraft.core.helpers.obfuscation.ObfuscationHelpers;

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
	private static final int TESSELATOR_BRIGHTNESS = 240;
	
	private ForgeDirection side;
	
	/**
	 * Make a new instance.
	 * @param world The world.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param z Z coordinate.
	 * @param side The side of the block to draw this particle on.
	 */
	public EntityBloodBrickFX(World world, double x, double y, double z, ForgeDirection side) {
		super(world, x, y, z, 0, 0, 0);
		this.side = side;
		
		this.motionX = 0;
		this.motionY = 0;
		this.motionZ = 0;
		
		this.particleRed = 255;
		this.particleGreen = 255;
		this.particleBlue = 255;
		this.particleGravity = 0;
		
		//this.particleScale *= scale;
		//this.particleMaxAge = (int) ((rand.nextFloat() * 0.33F + 0.66F) * ageMultiplier);
		this.particleMaxAge = 40;
		this.noClip = true;
		this.setSize(0.01F, 0.01F);
		
		this.posX += (double) (side.offsetX) / 30D;
		this.posY += (double) (side.offsetY) / 30D;
		this.posZ += (double) (side.offsetZ) / 30D;
		
		this.prevPosX = posX;
		this.prevPosY = posY;
		this.prevPosZ = posZ;
	}

	@Override
	public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
		tessellator.draw();
		GL11.glPushMatrix();

		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		
		// Flipping to make sure the particle lines match the block lines.
		boolean flip = side == ForgeDirection.DOWN;
		Minecraft.getMinecraft().renderEngine.bindTexture(flip ? TEXTURE_FLIPPED : TEXTURE);

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);

		float f10 = 0.5F * particleScale;
		float f11 = (float)(prevPosX + (posX - prevPosX) * f - interpPosX);
		float f12 = (float)(prevPosY + (posY - prevPosY) * f - interpPosY);
		float f13 = (float)(prevPosZ + (posZ - prevPosZ) * f - interpPosZ);
		GL11.glTranslated(f11 + 0.5D, f12 + 0.5D, f13 + 0.5D);
		
		// Several rotations to make sure the particle lines match the block lines.
		if(side == ForgeDirection.WEST) { // X-
	    	GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
	    }
		if(side == ForgeDirection.NORTH) { // Z-
			GL11.glRotatef(90.0F, 0.0F, 0.0F, -1.0F);
		}
		if(side == ForgeDirection.EAST) { // X+
	    	GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
	    }
		if(side == ForgeDirection.SOUTH) { // Z+
			GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
		}
		if(side == ForgeDirection.DOWN) { // Y-
			GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
		}
		
		GL11.glRotatef(90.0F, side.offsetY, -side.offsetX, side.offsetZ);
		
	    if(side.offsetZ > 0) {
	    	GL11.glTranslated(0.0D, 0.0D, 0.5D);
	    	GL11.glRotatef(180.0F, 0.0F, -1.0F, 0.0F);
	    } else {
	    	GL11.glTranslated(0.0D, 0.0D, -0.5D);
	    }
		
		tessellator.startDrawingQuads();
		tessellator.setBrightness(TESSELATOR_BRIGHTNESS);
		float brightness = (float) particleAge / (float) (particleMaxAge / 2);
		if(brightness > 1) brightness = (float) (particleMaxAge - particleAge) / 2 / (float) (particleMaxAge / 2);
		tessellator.setColorRGBA_F(particleRed, particleGreen, particleBlue, brightness);

		float offsetter = 1.0F;
		tessellator.addVertexWithUV(-0.5D * offsetter, 0.5D * offsetter, 0.0D, 0.0D, 1.0D);
	    tessellator.addVertexWithUV(0.5D * offsetter, 0.5D * offsetter, 0.0D, 1.0D, 1.0D);
	    tessellator.addVertexWithUV(0.5D * offsetter, -0.5D * offsetter, 0.0D, 1.0D, 0.0D);
	    tessellator.addVertexWithUV(-0.5D * offsetter, -0.5D * offsetter, 0.0D, 0.0D, 0.0D);
		tessellator.draw();
		
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);

		GL11.glPopMatrix();
		Minecraft.getMinecraft().renderEngine.bindTexture(ObfuscationHelpers.getParticleTexture());
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
		motionX *= 0.95D;
		motionY *= 0.95D;
		motionZ *= 0.95D;
	}

	/**
	 * Set the gravity for this particle.
	 * @param particleGravity The new gravity
	 */
	public void setGravity(float particleGravity) {
		this.particleGravity = particleGravity;
	}

}
