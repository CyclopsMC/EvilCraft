package org.cyclops.evilcraft.client.render.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.tileentity.TileSpiritPortal;
import org.lwjgl.opengl.GL11;

import java.util.Random;

/**
 * EvilCraft's version of a beacon renderer, this allows us to have custom colors
 * and customize other stuff without being dependend on vanilla code
 * 
 * @author immortaleeb
 *
 */
public class RenderTileEntitySpiritPortal extends TileEntitySpecialRenderer {

    private static final ResourceLocation PORTALBASE = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_MODELS + "portalBases.png");

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTickTime, int partialDamage) {
        renderTileEntityAt((TileSpiritPortal) tileentity, x, y, z, partialTickTime, partialDamage);
	}
	
	protected void renderTileEntityAt(TileSpiritPortal tileentity, double x, double y, double z, float partialTickTime, int partialDamage) {
        float progress = tileentity.getProgress();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.5F, 0.5f, 0.5F);
        renderPortalBase(Tessellator.getInstance().getBuffer(), (float) x, (float) y, (float) z, progress);
        GlStateManager.translate((float)x, (float)y, (float)z);
        Random random = new Random();
        long seed = tileentity.getPos().toLong();
        random.setSeed(seed);
        renderStar(seed, progress, Tessellator.getInstance(), partialTickTime, random);
        GlStateManager.popMatrix();
	}

    private void renderStar(float rotation, float progress, Tessellator tessellator, float partialTicks, Random random) {
        VertexBuffer worldRenderer = tessellator.getBuffer();

		/* Rotate opposite direction at 20% speed */
        GlStateManager.rotate(rotation * -0.2f % 360, 0.5f, 1, 0.5f);

		/* Configuration tweaks */
        float BEAM_START_DISTANCE = 2F;
        float BEAM_END_DISTANCE = 7f;
        float MAX_OPACITY = 40f;

        RenderHelper.disableStandardItemLighting();
        float f2 = 0.0F;

        if (progress > 0.8F) {
            f2 = (progress - 0.8F) / 0.2F;
        }

        GlStateManager.disableTexture2D();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GlStateManager.enableCull();
        GlStateManager.depthMask(false);

        for (int i = 0; i < (progress + progress * progress) / 2.0F * 60.0F; ++i) {
            GlStateManager.rotate(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(random.nextFloat() * 360.0F + progress * 90.0F, 0.0F, 0.0F, 1.0F);
            worldRenderer.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
            float f3 = random.nextFloat() * BEAM_END_DISTANCE + 5.0F + f2 * 10.0F;
            float f4 = random.nextFloat() * BEAM_START_DISTANCE + 1.0F + f2 * 2.0F;
            worldRenderer.pos(0.0D, 0.0D, 0.0D).color(171, 97, 210, (int)(MAX_OPACITY * (1.0F - f2))).endVertex();
            worldRenderer.pos(-0.866D * (double)f4, (double)f3, (double)(-0.5F * f4)).color(175, 100, 215, 0).endVertex();
            worldRenderer.pos(0.866D * (double)f4, (double)f3, (double)(-0.5F * f4)).color(175, 100, 215, 0).endVertex();
            worldRenderer.pos(0.0D, (double)f3, (double)(1.0F * f4)).color(175, 100, 215, 0).endVertex();
            worldRenderer.pos(-0.866D * (double)f4, (double)f3, (double)(-0.5F * f4)).color(175, 100, 215, 0).endVertex();
            tessellator.draw();
        }

        GlStateManager.depthMask(true);
        GlStateManager.disableCull();
        GlStateManager.disableBlend();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableTexture2D();
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        RenderHelper.enableStandardItemLighting();
    }

    private void renderPortalBase(VertexBuffer worldRenderer, float x, float y, float z, float progress) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.depthMask(false);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

        GlStateManager.color(0.72F, 0.5f, 0.83F);

        bindTexture(PORTALBASE);
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        float r = 180.0F - renderManager.playerViewY;
        GlStateManager.rotate(r, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-renderManager.playerViewX, 1F, 0F, 0F);
        renderIconForProgress(worldRenderer, ((int) (progress * 100)) % 4, progress);

        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
    }

    private void renderIconForProgress(VertexBuffer worldRenderer, int index, float progress) {
        if(progress > 0.8F) {
            progress -= (progress - 0.8F) * 4;
        }

        float u1 = .0625f * index;
        float u2 = .0625f * (index + 1);
        float v1 = 0;
        float v2 = .0625f;

        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5f * progress, 0.5f * progress, 0.5f * progress);
        GlStateManager.translate(-0.5F, -0.5f, 0);
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();

        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
        int j = 150;
        int k = 150;
        worldRenderer.pos(0, 1, 0.0D).tex(u1, v2).color(0.72F, 0.5f, 0.23F, 0.9F).lightmap(j, k).endVertex();
        worldRenderer.pos(0, 0, 0.0D).tex(u1, v1).color(0.72F, 0.5f, 0.83F, 0.9F).lightmap(j, k).endVertex();
        worldRenderer.pos(1, 0, 0.0D).tex(u2, v1).color(0.72F, 0.5f, 0.83F, 0.9F).lightmap(j, k).endVertex();
        worldRenderer.pos(1, 1, 0.0D).tex(u2, v2).color(0.72F, 0.5f, 0.83F, 0.9F).lightmap(j, k).endVertex();
        Tessellator.getInstance().draw();

        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
    }

}
