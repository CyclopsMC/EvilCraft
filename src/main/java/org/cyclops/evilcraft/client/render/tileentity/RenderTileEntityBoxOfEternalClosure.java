package org.cyclops.evilcraft.client.render.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.BoxOfEternalClosure;
import org.cyclops.evilcraft.client.render.model.ModelBoxOfEternalClosureBaked;
import org.cyclops.evilcraft.tileentity.TileBoxOfEternalClosure;

/**
 * Renderer for the {@link BoxOfEternalClosure}.
 * @author rubensworks
 *
 */
public class RenderTileEntityBoxOfEternalClosure extends TileEntitySpecialRenderer<TileBoxOfEternalClosure> {

	private static final ResourceLocation beamTexture =
			new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITIES + "beam.png");
    
    @Override
    public void renderTileEntityAt(TileBoxOfEternalClosure tile, double x, double y, double z, float partialTick, int destroyStage) {
        ResourceLocation texture = TextureMap.locationBlocksTexture;

        if (destroyStage >= 0) {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 4.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        } else if(texture != null) {
            this.bindTexture(texture);
        }

        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        GlStateManager.enableRescaleNormal();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.disableRescaleNormal();

        GlStateManager.translate(0.5F, 0.5F, 0.5F);
        EnumFacing direction = tile.getWorld().getBlockState(tile.getPos()).getValue(BoxOfEternalClosure.FACING);
        short rotation = 0;
        if (direction == EnumFacing.SOUTH) {
            rotation = 180;
        }
        if (direction == EnumFacing.NORTH) {
            rotation = 0;
        }
        if (direction == EnumFacing.WEST) {
            rotation = 90;
        }
        if (direction == EnumFacing.EAST) {
            rotation = -90;
        }
        GlStateManager.rotate((float) rotation, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);

        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().
                renderModelBrightnessColor(ModelBoxOfEternalClosureBaked.boxModel, 1.0F, 1.0F, 1.0F, 1.0F);

        float angle = tile.getPreviousLidAngle()
                + (tile.getLidAngle() - tile.getPreviousLidAngle()) * partialTick;
        GlStateManager.translate(0F, 0.375F, 0.25F);
        GlStateManager.rotate(-angle, 1F, 0F, 0);
        GlStateManager.translate(0F, -0.375F, -0.25F);
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().
                renderModelBrightnessColor(ModelBoxOfEternalClosureBaked.boxLidModel, 1.0F, 1.0F, 1.0F, 1.0F);

        // TODO: render beam
    	/*
    	// Make sure the beam originates from the center of the box.
    	x += 0.5D;
    	y -= 0.5D;
    	z += 0.5D;
    	
    	// Optionally render beam
    	VengeanceSpirit target = box.getTargetSpirit();
    	if(target != null) {
            // The 'bouncy' effect
    		float innerRotation = (float)box.innerRotation + partialTick;
            float yOffset = MathHelper.sin(innerRotation * 0.2F) / 4.0F + 0.5F;
            yOffset = (yOffset * yOffset + yOffset) * 0.2F;
            
            // Calculate the coordinates of the end of the beam
            float rotateX = -(target.width / 2) -(float)(box.getPos().getX() - target.posX - (target.prevPosX - target.posX) * (double)(1.0F - partialTick));
            float rotateY = (target.height / 2) - (float)((double)yOffset + box.getPos().getY() - target.posY - (target.prevPosY - target.posY) * (double)(1.0F - partialTick));
            float rotateZ = -(target.width / 2) -(float)(box.getPos().getZ() - target.posZ - (target.prevPosZ - target.posZ) * (double)(1.0F - partialTick));
            float distance = MathHelper.sqrt_float(rotateX * rotateX + rotateZ * rotateZ);
            
            // Set the scene coordinates right for the beam rendering
            GL11.glPushMatrix();
            GL11.glTranslatef((float)x, (float)y + 1.0F, (float)z);
            GL11.glRotatef((float)(-Math.atan2((double)rotateZ, (double)rotateX)) * 180.0F / (float)Math.PI - 90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef((float)(-Math.atan2((double)distance, (double)rotateY)) * 180.0F / (float)Math.PI - 90.0F, 1.0F, 0.0F, 0.0F);
            
            // Start tesselator
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_CULL_FACE);
            this.bindTexture(beamTexture);
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
            worldRenderer.startDrawing(GL11.GL_TRIANGLE_STRIP);
            
            // Calculate UV coordinates for the beam
            float zuv = MathHelper.sqrt_float(rotateX * rotateX + rotateY * rotateY + rotateZ * rotateZ);
            float v1 = MathHelper.sqrt_float(rotateX * rotateX + rotateY * rotateY
            		+ rotateZ * rotateZ) / 32.0F
            		- ((float)target.ticksExisted + partialTick) * 0.01F;
            float v2 = 0.0F - ((float)target.ticksExisted + partialTick) * 0.01F;
            
            // Draw the beam in a circle shape
            int amount = 8;
            for (int i = 0; i <= amount; ++i) {
                float xuv = MathHelper.sin((float)(i % amount) * (float)Math.PI * 2.0F / (float)amount) * 0.75F;
                float yuv = MathHelper.cos((float)(i % amount) * (float)Math.PI * 2.0F / (float)amount) * 0.75F;
                float u = (float)(i % amount) * 1.0F / (float)amount;
                worldRenderer.func_178991_c(0); // mcp: setColorOpaque
                worldRenderer.addVertexWithUV((double)(xuv * 0.2F), (double)(yuv * 0.2F), 0.0D, (double)u, (double)v1);
                worldRenderer.func_178991_c(16777215); // mcp: setColorOpaque
                worldRenderer.addVertexWithUV((double)xuv, (double)yuv, (double)zuv, (double)u, (double)v2);
            }

            // Finish drawing
            tessellator.draw();
            GL11.glEnable(GL11.GL_CULL_FACE);
            GlStateManager.shadeModel(GL11.GL_FLAT);
            RenderHelper.enableStandardItemLighting();
            GL11.glPopMatrix();
        }*/

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
    
}
