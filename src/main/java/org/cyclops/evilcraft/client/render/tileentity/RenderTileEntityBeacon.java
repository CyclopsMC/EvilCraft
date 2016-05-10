package org.cyclops.evilcraft.client.render.tileentity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.cyclops.evilcraft.tileentity.EvilCraftBeaconTileEntity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;

/**
 * EvilCraft's version of a beacon renderer, this allows us to have custom colors
 * and customize other stuff without being dependend on vanilla code
 * 
 * @author immortaleeb
 *
 */
public class RenderTileEntityBeacon extends TileEntitySpecialRenderer {
	
	private static final ResourceLocation BEACON_TEXTURE = new ResourceLocation("textures/entity/beacon_beam.png");
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTickTime, int partialDamage) {
		renderBeacon((EvilCraftBeaconTileEntity)tileentity, x, y, z, partialTickTime, partialDamage);
	}
	
	protected void renderBeacon(EvilCraftBeaconTileEntity tileentity, double x, double y, double z, float partialTickTime, int partialDamage) {
		float f1 = tileentity.getBeamRenderVariable();
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
		
        if (tileentity.isBeamActive())
        {
        	Vector4f beamInnerColor = tileentity.getBeamInnerColor();
        	Vector4f beamOuterColor = tileentity.getBeamOuterColor();
        	
            Tessellator tessellator = Tessellator.getInstance();
            VertexBuffer worldRenderer = tessellator.getBuffer();
            this.bindTexture(BEACON_TEXTURE);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
            GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
            float f2 = (float)tileentity.getWorld().getTotalWorldTime() + partialTickTime;
            float f3 = -f2 * 0.2F - (float) MathHelper.floor_float(-f2 * 0.1F);
            byte b0 = 1;
            double d3 = (double)f2 * 0.025D * (1.0D - (double)(b0 & 1) * 2.5D);
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            double d4 = (double)b0 * 0.2D;
            double d5 = 0.5D + Math.cos(d3 + 2.356194490192345D) * d4;
            double d6 = 0.5D + Math.sin(d3 + 2.356194490192345D) * d4;
            double d7 = 0.5D + Math.cos(d3 + (Math.PI / 4D)) * d4;
            double d8 = 0.5D + Math.sin(d3 + (Math.PI / 4D)) * d4;
            double d9 = 0.5D + Math.cos(d3 + 3.9269908169872414D) * d4;
            double d10 = 0.5D + Math.sin(d3 + 3.9269908169872414D) * d4;
            double d11 = 0.5D + Math.cos(d3 + 5.497787143782138D) * d4;
            double d12 = 0.5D + Math.sin(d3 + 5.497787143782138D) * d4;
            double d13 = (double)(256.0F * f1);
            double d14 = 0.0D;
            double d15 = 1.0D;
            double d16 = (double)(-1.0F + f3);
            double d17 = (double)(256.0F * f1) * (0.5D / d4) + d16;
            worldRenderer.pos(x + d5, y + d13, z + d6).tex(d15, d17).color(beamInnerColor.x, beamInnerColor.y, beamInnerColor.z, beamInnerColor.w).endVertex();
            worldRenderer.pos(x + d5, y, z + d6).tex(d15, d16).color(beamInnerColor.x, beamInnerColor.y, beamInnerColor.z, beamInnerColor.w).endVertex();
            worldRenderer.pos(x + d7, y, z + d8).tex(d14, d16).color(beamInnerColor.x, beamInnerColor.y, beamInnerColor.z, beamInnerColor.w).endVertex();
            worldRenderer.pos(x + d7, y + d13, z + d8).tex(d14, d17).color(beamInnerColor.x, beamInnerColor.y, beamInnerColor.z, beamInnerColor.w).endVertex();
            worldRenderer.pos(x + d11, y + d13, z + d12).tex(d15, d17).color(beamInnerColor.x, beamInnerColor.y, beamInnerColor.z, beamInnerColor.w).endVertex();
            worldRenderer.pos(x + d11, y, z + d12).tex(d15, d16).color(beamInnerColor.x, beamInnerColor.y, beamInnerColor.z, beamInnerColor.w).endVertex();
            worldRenderer.pos(x + d9, y, z + d10).tex(d14, d16).color(beamInnerColor.x, beamInnerColor.y, beamInnerColor.z, beamInnerColor.w).endVertex();
            worldRenderer.pos(x + d9, y + d13, z + d10).tex(d14, d17).color(beamInnerColor.x, beamInnerColor.y, beamInnerColor.z, beamInnerColor.w).endVertex();
            worldRenderer.pos(x + d7, y + d13, z + d8).tex(d15, d17).color(beamInnerColor.x, beamInnerColor.y, beamInnerColor.z, beamInnerColor.w).endVertex();
            worldRenderer.pos(x + d7, y, z + d8).tex(d15, d16).color(beamInnerColor.x, beamInnerColor.y, beamInnerColor.z, beamInnerColor.w).endVertex();
            worldRenderer.pos(x + d11, y, z + d12).tex(d14, d16).color(beamInnerColor.x, beamInnerColor.y, beamInnerColor.z, beamInnerColor.w).endVertex();
            worldRenderer.pos(x + d11, y + d13, z + d12).tex(d14, d17).color(beamInnerColor.x, beamInnerColor.y, beamInnerColor.z, beamInnerColor.w).endVertex();
            worldRenderer.pos(x + d9, y + d13, z + d10).tex(d15, d17).color(beamInnerColor.x, beamInnerColor.y, beamInnerColor.z, beamInnerColor.w).endVertex();
            worldRenderer.pos(x + d9, y, z + d10).tex(d15, d16).color(beamInnerColor.x, beamInnerColor.y, beamInnerColor.z, beamInnerColor.w).endVertex();
            worldRenderer.pos(x + d5, y, z + d6).tex(d14, d16).color(beamInnerColor.x, beamInnerColor.y, beamInnerColor.z, beamInnerColor.w).endVertex();
            worldRenderer.pos(x + d5, y + d13, z + d6).tex(d14, d17).color(beamInnerColor.x, beamInnerColor.y, beamInnerColor.z, beamInnerColor.w).endVertex();
            tessellator.draw();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            double d18 = 0.2D;
            double d19 = 0.2D;
            double d20 = 0.8D;
            double d21 = 0.2D;
            double d22 = 0.2D;
            double d23 = 0.8D;
            double d24 = 0.8D;
            double d25 = 0.8D;
            double d26 = (double)(256.0F * f1);
            double d27 = 0.0D;
            double d28 = 1.0D;
            double d29 = (double)(-1.0F + f3);
            double d30 = (double)(256.0F * f1) + d29;
            worldRenderer.pos(x + d18, y + d26, z + d19).tex(d28, d30).color(beamOuterColor.x, beamOuterColor.y, beamOuterColor.z, beamOuterColor.w).endVertex();
            worldRenderer.pos(x + d18, y, z + d19).tex(d28, d29).color(beamOuterColor.x, beamOuterColor.y, beamOuterColor.z, beamOuterColor.w).endVertex();
            worldRenderer.pos(x + d20, y, z + d21).tex(d27, d29).color(beamOuterColor.x, beamOuterColor.y, beamOuterColor.z, beamOuterColor.w).endVertex();
            worldRenderer.pos(x + d20, y + d26, z + d21).tex(d27, d30).color(beamOuterColor.x, beamOuterColor.y, beamOuterColor.z, beamOuterColor.w).endVertex();
            worldRenderer.pos(x + d24, y + d26, z + d25).tex(d28, d30).color(beamOuterColor.x, beamOuterColor.y, beamOuterColor.z, beamOuterColor.w).endVertex();
            worldRenderer.pos(x + d24, y, z + d25).tex(d28, d29).color(beamOuterColor.x, beamOuterColor.y, beamOuterColor.z, beamOuterColor.w).endVertex();
            worldRenderer.pos(x + d22, y, z + d23).tex(d27, d29).color(beamOuterColor.x, beamOuterColor.y, beamOuterColor.z, beamOuterColor.w).endVertex();
            worldRenderer.pos(x + d22, y + d26, z + d23).tex(d27, d30).color(beamOuterColor.x, beamOuterColor.y, beamOuterColor.z, beamOuterColor.w).endVertex();
            worldRenderer.pos(x + d20, y + d26, z + d21).tex(d28, d30).color(beamOuterColor.x, beamOuterColor.y, beamOuterColor.z, beamOuterColor.w).endVertex();
            worldRenderer.pos(x + d20, y, z + d21).tex(d28, d29).color(beamOuterColor.x, beamOuterColor.y, beamOuterColor.z, beamOuterColor.w).endVertex();
            worldRenderer.pos(x + d24, y, z + d25).tex(d27, d29).color(beamOuterColor.x, beamOuterColor.y, beamOuterColor.z, beamOuterColor.w).endVertex();
            worldRenderer.pos(x + d24, y + d26, z + d25).tex(d27, d30).color(beamOuterColor.x, beamOuterColor.y, beamOuterColor.z, beamOuterColor.w).endVertex();
            worldRenderer.pos(x + d22, y + d26, z + d23).tex(d28, d30).color(beamOuterColor.x, beamOuterColor.y, beamOuterColor.z, beamOuterColor.w).endVertex();
            worldRenderer.pos(x + d22, y, z + d23).tex(d28, d29).color(beamOuterColor.x, beamOuterColor.y, beamOuterColor.z, beamOuterColor.w).endVertex();
            worldRenderer.pos(x + d18, y, z + d19).tex(d27, d29).color(beamOuterColor.x, beamOuterColor.y, beamOuterColor.z, beamOuterColor.w).endVertex();
            worldRenderer.pos(x + d18, y + d26, z + d19).tex(d27, d30).color(beamOuterColor.x, beamOuterColor.y, beamOuterColor.z, beamOuterColor.w).endVertex();
            tessellator.draw();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
            GlStateManager.depthMask(true);
        }
        
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.5F);
	}

}
