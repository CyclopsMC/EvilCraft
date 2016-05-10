package org.cyclops.evilcraft.client.render.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.BoxOfEternalClosure;
import org.cyclops.evilcraft.client.render.model.ModelBoxOfEternalClosureBaked;
import org.cyclops.evilcraft.entity.monster.VengeanceSpirit;
import org.cyclops.evilcraft.tileentity.TileBoxOfEternalClosure;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;
import java.util.Random;

/**
 * Renderer for the {@link BoxOfEternalClosure}.
 * @author rubensworks
 *
 */
public class RenderTileEntityBoxOfEternalClosure extends TileEntitySpecialRenderer<TileBoxOfEternalClosure> {

    private static final ResourceLocation END_SKY_TEXTURE = new ResourceLocation("textures/environment/end_sky.png");
    private static final ResourceLocation END_PORTAL_TEXTURE = new ResourceLocation("textures/entity/end_portal.png");
    private static final Random field_147527_e = new Random(31100L);
	private static final ResourceLocation beamTexture =
			new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITIES + "beam.png");

    FloatBuffer field_147528_b = GLAllocation.createDirectFloatBuffer(16);
    
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
        GlStateManager.pushMatrix();
        GlStateManager.translate(0F, 0.375F, 0.25F);
        GlStateManager.rotate(-angle, 1F, 0F, 0);
        GlStateManager.translate(0F, -0.375F, -0.25F);
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().
                renderModelBrightnessColor(ModelBoxOfEternalClosureBaked.boxLidModel, 1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();

        if(angle > 0) {
            renderEnd(x, y, z, getWorld().getBlockState(tile.getPos()).getValue(BoxOfEternalClosure.FACING).getAxis(), tile.getPos().toLong());
        }

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

    	// Make sure the beam originates from the center of the box.
    	x += 0.5D;
    	y -= 0.5D;
    	z += 0.5D;
    	
    	// Optionally render beam
    	VengeanceSpirit target = tile.getTargetSpirit();
    	if(target != null) {
            // The 'bouncy' effect
    		float innerRotation = (float)tile.innerRotation + partialTick;
            float yOffset = MathHelper.sin(innerRotation * 0.2F) / 4.0F + 0.5F;
            yOffset = (yOffset * yOffset + yOffset) * 0.2F;
            
            // Calculate the coordinates of the end of the beam
            float rotateX = -(target.width / 2) -(float)(tile.getPos().getX() - target.posX - (target.prevPosX - target.posX) * (double)(1.0F - partialTick));
            float rotateY = (target.height / 2) - (float)((double)yOffset + tile.getPos().getY() - target.posY - (target.prevPosY - target.posY) * (double)(1.0F - partialTick));
            float rotateZ = -(target.width / 2) -(float)(tile.getPos().getZ() - target.posZ - (target.prevPosZ - target.posZ) * (double)(1.0F - partialTick));
            float distance = MathHelper.sqrt_float(rotateX * rotateX + rotateZ * rotateZ);
            
            // Set the scene coordinates right for the beam rendering
            GL11.glPushMatrix();
            GL11.glTranslatef((float)x, (float)y + 1.0F, (float)z);
            GL11.glRotatef((float)(-Math.atan2((double)rotateZ, (double)rotateX)) * 180.0F / (float)Math.PI - 90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef((float)(-Math.atan2((double)distance, (double)rotateY)) * 180.0F / (float)Math.PI - 90.0F, 1.0F, 0.0F, 0.0F);
            
            // Start tesselator
            Tessellator tessellator = Tessellator.getInstance();
            VertexBuffer worldRenderer = tessellator.getBuffer();
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_CULL_FACE);
            this.bindTexture(beamTexture);
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
            worldRenderer.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_TEX_COLOR);
            
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
                worldRenderer.pos((double)(xuv * 0.2F), (double)(yuv * 0.2F), 0.0D).tex((double)u, (double)v2).color(0, 0, 0, 255).endVertex();
                worldRenderer.pos((double)xuv, (double)yuv, (double)zuv).tex((double)u, (double)v1).color(255, 255, 255, 255).endVertex();
            }

            // Finish drawing
            tessellator.draw();
            GL11.glEnable(GL11.GL_CULL_FACE);
            GlStateManager.shadeModel(GL11.GL_FLAT);
            RenderHelper.enableStandardItemLighting();
            GL11.glPopMatrix();
        }
    }

    // Copied and adapted from TileEntityEndPortalRenderer
    protected void renderEnd(double x, double y, double z, EnumFacing.Axis axis, long seedOffset) {
        float f = (float)this.rendererDispatcher.entityX;
        float f1 = (float)this.rendererDispatcher.entityY;
        float f2 = (float)this.rendererDispatcher.entityZ;
        GlStateManager.disableLighting();
        field_147527_e.setSeed(31100L + seedOffset);
        float f3 = 0.25F;

        for (int i = 0; i < 16; ++i)
        {
            GlStateManager.pushMatrix();
            float f4 = (float)(16 - i);
            float f5 = 0.0625F;
            float f6 = 1.0F / (f4 + 1.0F);

            if (i == 0)
            {
                this.bindTexture(END_SKY_TEXTURE);
                f6 = 0.1F;
                f4 = 65.0F;
                f5 = 0.125F;
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(770, 771);
            }

            if (i >= 1)
            {
                this.bindTexture(END_PORTAL_TEXTURE);
            }

            if (i == 1)
            {
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(1, 1);
                f5 = 0.5F;
            }

            float f7 = (float)(-(y + (double)f3));
            float f8 = f7 + (float) ActiveRenderInfo.getPosition().yCoord;
            float f9 = f7 + f4 + (float)ActiveRenderInfo.getPosition().yCoord;
            float f10 = f8 / f9;
            f10 = (float)(y + (double)f3) + f10;
            GlStateManager.translate(f, f10, f2);
            GlStateManager.texGen(GlStateManager.TexGen.S, 9217);
            GlStateManager.texGen(GlStateManager.TexGen.T, 9217);
            GlStateManager.texGen(GlStateManager.TexGen.R, 9217);
            GlStateManager.texGen(GlStateManager.TexGen.Q, 9216);
            GlStateManager.texGen(GlStateManager.TexGen.S, 9473, this.func_147525_a(1.0F, 0.0F, 0.0F, 0.0F));
            GlStateManager.texGen(GlStateManager.TexGen.T, 9473, this.func_147525_a(0.0F, 0.0F, 1.0F, 0.0F));
            GlStateManager.texGen(GlStateManager.TexGen.R, 9473, this.func_147525_a(0.0F, 0.0F, 0.0F, 1.0F));
            GlStateManager.texGen(GlStateManager.TexGen.Q, 9474, this.func_147525_a(0.0F, 1.0F, 0.0F, 0.0F));
            GlStateManager.enableTexGenCoord(GlStateManager.TexGen.S);
            GlStateManager.enableTexGenCoord(GlStateManager.TexGen.T);
            GlStateManager.enableTexGenCoord(GlStateManager.TexGen.R);
            GlStateManager.enableTexGenCoord(GlStateManager.TexGen.Q);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0F, (float)((Minecraft.getSystemTime() + seedOffset) % 700000L) / 700000.0F, 0.0F);
            GlStateManager.scale(f5, f5, f5);
            GlStateManager.translate(0.5F, 0.5F, 0.0F);
            GlStateManager.rotate((float)(i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.translate(-0.5F, -0.5F, 0.0F);
            GlStateManager.translate(-f, -f2, -f1);
            f8 = f7 + (float)ActiveRenderInfo.getPosition().yCoord;
            GlStateManager.translate((float)ActiveRenderInfo.getPosition().xCoord * f4 / f8, (float)ActiveRenderInfo.getPosition().zCoord * f4 / f8, -f1);
            Tessellator tessellator = Tessellator.getInstance();
            VertexBuffer worldrenderer = tessellator.getBuffer();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            float f11 = (field_147527_e.nextFloat() * 0.5F + 0.5F) * f6;
            float f12 = (field_147527_e.nextFloat() * 0.5F + 0.2F) * f6;
            float f13 = (field_147527_e.nextFloat() * 0.5F + 0.2F) * f6;

            if (i == 0)
            {
                f11 = f12 = f13 = 1.0F * f6;
            }

            double edgeX = axis == EnumFacing.Axis.Y ? 0.3125D : 0;
            double edgeZ = axis == EnumFacing.Axis.Y ? 0 : 0.3125D;
            worldrenderer.pos(0 + edgeX, (double)f3, 0.0D + edgeZ).color(f11, f12, f13, 1.0F).endVertex();
            worldrenderer.pos(0 + edgeX, (double)f3, 1.0D - edgeZ).color(f11, f12, f13, 1.0F).endVertex();
            worldrenderer.pos(1.0D - edgeX, (double)f3, 1.0D - edgeZ).color(f11, f12, f13, 1.0F).endVertex();
            worldrenderer.pos(1.0D - edgeX, (double)f3, 0.0D + edgeZ).color(f11, f12, f13, 1.0F).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
            this.bindTexture(END_SKY_TEXTURE);
        }

        GlStateManager.blendFunc(1, 1);
        GlStateManager.disableTexGenCoord(GlStateManager.TexGen.S);
        GlStateManager.disableTexGenCoord(GlStateManager.TexGen.T);
        GlStateManager.disableTexGenCoord(GlStateManager.TexGen.R);
        GlStateManager.disableTexGenCoord(GlStateManager.TexGen.Q);
        GlStateManager.enableLighting();
    }

    private FloatBuffer func_147525_a(float p_147525_1_, float p_147525_2_, float p_147525_3_, float p_147525_4_)
    {
        this.field_147528_b.clear();
        this.field_147528_b.put(p_147525_1_).put(p_147525_2_).put(p_147525_3_).put(p_147525_4_);
        this.field_147528_b.flip();
        return this.field_147528_b;
    }
    
}
