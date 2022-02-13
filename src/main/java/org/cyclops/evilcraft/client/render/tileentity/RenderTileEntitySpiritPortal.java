package org.cyclops.evilcraft.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.tileentity.TileSpiritPortal;

import java.util.Random;

/**
 * EvilCraft's version of a beacon renderer, this allows us to have custom colors
 * and customize other stuff without being dependend on vanilla code
 * 
 * @author immortaleeb
 *
 */
public class RenderTileEntitySpiritPortal extends TileEntityRenderer<TileSpiritPortal> {

    private static final ResourceLocation PORTALBASE = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_MODELS + "portal_bases.png");

    public RenderTileEntitySpiritPortal(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
	public void render(TileSpiritPortal tileentity, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        float progress = tileentity.getProgress();
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.5F, 0.5f, 0.5F);
        renderPortalBase(matrixStackIn, bufferIn, progress);
        Random random = new Random();
        long seed = tileentity.getBlockPos().asLong();
        random.setSeed(seed);
        renderStar(matrixStackIn, bufferIn, seed, progress, Tessellator.getInstance(), partialTicks, random);
        matrixStackIn.popPose();
	}

    private void renderStar(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, float rotation, float progress, Tessellator tessellator, float partialTicks, Random random) {
		/* Rotate opposite direction at 20% speed */
        matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(rotation * -0.2f % 360 / 2));
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(rotation * -0.2f % 360));
        matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(rotation * -0.2f % 360 / 2));

		/* Configuration tweaks */
        float BEAM_START_DISTANCE = 2F;
        float BEAM_END_DISTANCE = 7f;
        float MAX_OPACITY = 40f;

        RenderHelper.turnOff();
        float f2 = 0.0F;

        if (progress > 0.8F) {
            f2 = (progress - 0.8F) / 0.2F;
        }

        for (int i = 0; i < (progress + progress * progress) / 2.0F * 60.0F; ++i) {
            matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(random.nextFloat() * 360.0F));
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(random.nextFloat() * 360.0F));
            matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360.0F));
            matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(random.nextFloat() * 360.0F));
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(random.nextFloat() * 360.0F));
            matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360.0F + progress * 90.0F));
            float f3 = random.nextFloat() * BEAM_END_DISTANCE + 5.0F + f2 * 10.0F;
            float f4 = random.nextFloat() * BEAM_START_DISTANCE + 1.0F + f2 * 2.0F;
            IVertexBuilder vb = bufferIn.getBuffer(RenderType.lightning());
            Matrix4f matrix = matrixStackIn.last().pose();
            vb.vertex(matrix, 0, 0, 0).color(171, 97, 210, (int)(MAX_OPACITY * (1.0F - f2))).endVertex();
            vb.vertex(matrix, -0.866F * f4, f3, (-0.5F * f4)).color(175, 100, 215, 0).endVertex();
            vb.vertex(matrix, 0.866F * f4, f3, (-0.5F * f4)).color(175, 100, 215, 0).endVertex();
            vb.vertex(matrix, 0.0F, f3, (1.0F * f4)).color(175, 100, 215, 0).endVertex();
            vb.vertex(matrix, -0.866F * f4, f3, (-0.5F * f4)).color(175, 100, 215, 0).endVertex();
        }
    }

    private void renderPortalBase(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, float progress) {
        matrixStackIn.pushPose();

        EntityRendererManager renderManager = Minecraft.getInstance().getEntityRenderDispatcher();
        matrixStackIn.mulPose(renderManager.cameraOrientation());
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        renderIconForProgress(matrixStackIn, bufferIn, ((int) (progress * 100)) % 4, progress);

        matrixStackIn.popPose();
    }

    private void renderIconForProgress(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int index, float progress) {
        if(progress > 0.8F) {
            progress -= (progress - 0.8F) * 4;
        }

        float u1 = .0625f * index;
        float u2 = .0625f * (index + 1);
        float v1 = 0;
        float v2 = .0625f;

        matrixStackIn.pushPose();
        matrixStackIn.scale(0.5f * progress, 0.5f * progress, 0.5f * progress);
        matrixStackIn.translate(-0.5F, -0.5f, 0);

        int j = 150;
        int k = 150;
        IVertexBuilder vb = bufferIn.getBuffer(RenderType.text(PORTALBASE));
        Matrix4f matrix = matrixStackIn.last().pose();
        vb.vertex(matrix, 0, 1, 0).color(0.72F, 0.5f, 0.23F, 0.9F).uv(u1, v2).uv2(j, k).endVertex();
        vb.vertex(matrix, 0, 0, 0).color(0.72F, 0.5f, 0.83F, 0.9F).uv(u1, v1).uv2(j, k).endVertex();
        vb.vertex(matrix, 1, 0, 0).color(0.72F, 0.5f, 0.83F, 0.9F).uv(u2, v1).uv2(j, k).endVertex();
        vb.vertex(matrix, 1, 1, 0).color(0.72F, 0.5f, 0.83F, 0.9F).uv(u2, v2).uv2(j, k).endVertex();

        matrixStackIn.popPose();
    }

}
