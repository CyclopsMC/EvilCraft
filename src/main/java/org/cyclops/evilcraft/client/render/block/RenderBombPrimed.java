package org.cyclops.evilcraft.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.entity.TntRenderer;
import net.minecraft.client.renderer.entity.state.TntRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;

/**
 * Renderer for a primed bomb.
 * @author rubensworks
 *
 */
public class RenderBombPrimed extends TntRenderer {

    protected final BlockRenderDispatcher blockRenderer;
    protected final Block block;

    public RenderBombPrimed(EntityRendererProvider.Context renderContext, Block block) {
        super(renderContext);
        this.blockRenderer = renderContext.getBlockRenderDispatcher();
        this.block = block;
    }

    @Override
    public void submit(TntRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        super.submit(renderState, poseStack, submitNodeCollector, cameraRenderState);

        poseStack.pushPose();
        poseStack.translate(0.0D, 0.5D, 0.0D);
        if (renderState.fuseRemainingInTicks < 10.0F) {
            float f = 1.0F - renderState.fuseRemainingInTicks / 10.0F;
            f = Mth.clamp(f, 0.0F, 1.0F);
            f = f * f;
            f = f * f;
            float f1 = 1.0F + f * 0.3F;
            poseStack.scale(f1, f1, f1);
        }

        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
        poseStack.translate(-0.5D, -0.5D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        TntMinecartRenderer.submitWhiteSolidBlock(this.block.defaultBlockState(), poseStack, submitNodeCollector, renderState.lightCoords, renderState.fuseRemainingInTicks / 5 % 2 == 0, renderState.outlineColor);
        poseStack.popPose();
        //super.render(entityIn, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
    }

}
