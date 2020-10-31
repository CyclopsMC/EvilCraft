package org.cyclops.evilcraft.client.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.evilcraft.Reference;
import org.lwjgl.opengl.GL11;

/**
 * A blurred static fading particle with any possible color.
 * Based on Vazkii's FXWisp's from Botania.
 * @author rubensworks
 *
 */
public class ParticleBloodBrick extends SpriteTexturedParticle {

	private static final ParticleBloodBrick.RenderType RENDER_TYPE = new ParticleBloodBrick.RenderType();
	
	private static final ResourceLocation TEXTURE = new ResourceLocation(
			Reference.MOD_ID, Reference.TEXTURE_PATH_PARTICLES + "blood_brick_activation.png");
	private static final ResourceLocation TEXTURE_FLIPPED = new ResourceLocation(
			Reference.MOD_ID, Reference.TEXTURE_PATH_PARTICLES + "blood_brick_activation_flipped.png");
	private static final int TESSELATOR_BRIGHTNESS = 160;
	
	private Direction side;
	
	/**
	 * Make a new instance.
	 * @param world The world.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param z Z coordinate.
	 * @param side The side of the blockState to draw this particle on.
	 */
	public ParticleBloodBrick(World world, double x, double y, double z, Direction side) {
		super(world, x, y, z, 0, 0, 0);
		this.side = side;
		
		this.motionX = 0;
		this.motionY = 0;
		this.motionZ = 0;
		
		this.particleRed = 1;
		this.particleGreen = 1;
		this.particleBlue = 1;
		this.particleGravity = 0;
		
		//this.particleScale *= scale;
		//this.maxAge = (int) ((rand.nextFloat() * 0.33F + 0.66F) * ageMultiplier);
		this.maxAge = 40;
		this.setSize(0.01F, 0.01F);
		
		this.posX += (double) (side.getXOffset()) / 30D;
		this.posY += (double) (side.getYOffset()) / 30D;
		this.posZ += (double) (side.getZOffset()) / 30D;
		
		this.prevPosX = posX;
		this.prevPosY = posY;
		this.prevPosZ = posZ;
	}

	public IParticleRenderType getRenderType() {
		return RENDER_TYPE;
	}

	@Override
	public void tick() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		if(age++ >= maxAge) {
			setExpired();
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

	@Override
	public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks) {
		// Flipping to make sure the particle lines match the blockState lines.
		boolean flip = side == Direction.DOWN;
		RenderHelpers.bindTexture(flip ? TEXTURE_FLIPPED : TEXTURE);

		Vec3d vec3d = renderInfo.getProjectedView();
		float f11 = (float)(prevPosX + (posX - prevPosX) * partialTicks - vec3d.getX());
		float f12 = (float)(prevPosY + (posY - prevPosY) * partialTicks - vec3d.getY());
		float f13 = (float)(prevPosZ + (posZ - prevPosZ) * partialTicks - vec3d.getZ());

		float brightness = (float) age / (float) (maxAge / 2);
		if(brightness > 1) brightness = (float) (maxAge - age) / 2 / (float) (maxAge / 2);

		float offsetter = 1.0F;
		int i = TESSELATOR_BRIGHTNESS;
		int j = i >> 16 & 65535;
		int k = i & 65535;

		// Vector positions
		Vector3f[] avector3f = new Vector3f[]{
				new Vector3f(-0.5F * offsetter, 0.5F * offsetter, 0.0F),
				new Vector3f(0.5F * offsetter, 0.5F * offsetter, 0.0F),
				new Vector3f(0.5F * offsetter, -0.5F * offsetter, 0.0F),
				new Vector3f(-0.5F * offsetter, -0.5F * offsetter, 0.0F)
		};

		float f4 = this.getScale(partialTicks);
		// Transform position
		for(int vi = 0; vi < 4; ++vi) {
			Vector3f vector3f = avector3f[vi];

			// Offset
			vector3f.add(f11 + 0.5F, f12 + 0.5F, f13 + 0.5F);

			// Scale
			vector3f.mul(f4);

			// Several rotations to make sure the particle lines match the blockState lines.
			Quaternion transf = new Quaternion(renderInfo.getRotation());
			transf.multiply(Vector3f.XP.rotation(side.getYOffset() * 90));
			transf.multiply(Vector3f.YP.rotation(- side.getXOffset() * 90));
			transf.multiply(Vector3f.ZP.rotation(side.getZOffset() * 90));
			if(side == Direction.WEST) { // X-
				transf.multiply(Vector3f.XP.rotation(180));
			}
			if(side == Direction.NORTH) { // Z-
				transf.multiply(Vector3f.ZP.rotation(-90));
			}
			if(side == Direction.EAST) { // X+
				transf.multiply(Vector3f.XP.rotation(180));
			}
			if(side == Direction.SOUTH) { // Z+
				transf.multiply(Vector3f.ZP.rotation(90));
			}
			if(side == Direction.DOWN) { // Y-
				transf.multiply(Vector3f.YP.rotation(180));
			}
			vector3f.transform(transf);

			if(side.getZOffset() > 0) {
				vector3f.add(0, 0, 0.5F);
				Quaternion rot = new Quaternion(renderInfo.getRotation());
				rot.multiply(Vector3f.YP.rotation(-180));
				vector3f.transform(rot);
			} else {
				vector3f.add(0, 0, -0.5F);
			}
		}

		buffer.pos(avector3f[0].getX(), avector3f[0].getY(), avector3f[0].getZ()).tex(0.0F, 1.0F)
				.color(particleRed, particleGreen, particleBlue, brightness).lightmap(j, k).endVertex();
		buffer.pos(avector3f[1].getX(), avector3f[1].getY(), avector3f[1].getZ()).tex(1.0F, 1.0F)
				.color(particleRed, particleGreen, particleBlue, brightness).lightmap(j, k).endVertex();
		buffer.pos(avector3f[2].getX(), avector3f[2].getY(), avector3f[2].getZ()).tex(1.0F, 0.0F)
				.color(particleRed, particleGreen, particleBlue, brightness).lightmap(j, k).endVertex();
		buffer.pos(avector3f[3].getX(), avector3f[3].getY(), avector3f[3].getZ()).tex(0.0F, 0.0F)
				.color(particleRed, particleGreen, particleBlue, brightness).lightmap(j, k).endVertex();
	}

	public static class RenderType implements IParticleRenderType {
		public RenderType() {
		}

		public void beginRender(BufferBuilder bufferBuilder, TextureManager textureManager) {
			RenderSystem.depthMask(false);
			RenderSystem.enableBlend();
			RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			RenderSystem.alphaFunc(516, 0.003921569F);
			RenderSystem.disableLighting();
			textureManager.bindTexture(AtlasTexture.LOCATION_PARTICLES_TEXTURE);
			textureManager.getTexture(AtlasTexture.LOCATION_PARTICLES_TEXTURE).setBlurMipmap(true, false);
			bufferBuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
		}

		public void finishRender(Tessellator tessellator) {
			tessellator.draw();
			Minecraft.getInstance().textureManager.getTexture(AtlasTexture.LOCATION_PARTICLES_TEXTURE).restoreLastBlurMipmap();
			RenderSystem.alphaFunc(516, 0.1F);
			RenderSystem.disableBlend();
			RenderSystem.depthMask(true);
		}
	}

}
