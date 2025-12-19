package org.cyclops.evilcraft.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.blockentity.BlockEntitySpiritPortal;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

/**
 * EvilCraft's version of a beacon renderer, this allows us to have custom colors
 * and customize other stuff without being dependend on vanilla code
 *
 * @author immortaleeb
 *
 */
public class RenderBlockEntitySpiritPortal implements BlockEntityRenderer<BlockEntitySpiritPortal, RenderBlockEntitySpiritPortal.RenderState> {

    private static final Identifier PORTALBASE = Identifier.fromNamespaceAndPath(Reference.MOD_ID, Reference.TEXTURE_PATH_MODELS + "portal_bases.png");

    public RenderBlockEntitySpiritPortal(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public AABB getRenderBoundingBox(BlockEntitySpiritPortal blockEntity) {
        return AABB.INFINITE;
    }

    @Override
    public RenderState createRenderState() {
        return new RenderState();
    }

    @Override
    public void extractRenderState(BlockEntitySpiritPortal blockEntity, RenderState renderState, float partialTick, Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        renderState.progress = blockEntity.getProgress();
        renderState.partialTicks = partialTick;
    }

    @Override
    public void submit(RenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        float progress = renderState.progress;
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5f, 0.5F);
        renderPortalBase(poseStack, submitNodeCollector, cameraRenderState, progress);
        Random random = new Random();
        long seed = renderState.blockPos.asLong();
        random.setSeed(seed);
        renderStar(poseStack, submitNodeCollector, seed, progress, Tesselator.getInstance(), renderState.partialTicks, random);
        poseStack.popPose();
    }

    private void renderStar(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, float rotation, float progress, Tesselator tessellator, float partialTicks, Random random) {
        /* Rotate opposite direction at 20% speed */
        poseStack.mulPose(Axis.XP.rotationDegrees(rotation * -0.2f % 360 / 2));
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation * -0.2f % 360));
        poseStack.mulPose(Axis.ZP.rotationDegrees(rotation * -0.2f % 360 / 2));

        /* Configuration tweaks */
        float BEAM_START_DISTANCE = 2F;
        float BEAM_END_DISTANCE = 7f;
        float MAX_OPACITY = 40f;

        float f2;
        if (progress > 0.8F) {
            f2 = (progress - 0.8F) / 0.2F;
        } else {
            f2 = 0.0F;
        }

        for (int i = 0; i < (progress + progress * progress) / 2.0F * 60.0F; ++i) {
            poseStack.mulPose(Axis.XP.rotationDegrees(random.nextFloat() * 360.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(random.nextFloat() * 360.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(random.nextFloat() * 360.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(random.nextFloat() * 360.0F + progress * 90.0F));
            float f3 = random.nextFloat() * BEAM_END_DISTANCE + 5.0F + f2 * 10.0F;
            float f4 = random.nextFloat() * BEAM_START_DISTANCE + 1.0F + f2 * 2.0F;
            submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.lightning(), (pose, vb) -> {
                vb.addVertex(pose, 0, 0, 0).setColor(171, 97, 210, (int)(MAX_OPACITY * (1.0F - f2)));
                vb.addVertex(pose, -0.866F * f4, f3, (-0.5F * f4)).setColor(175, 100, 215, 0);
                vb.addVertex(pose, 0.866F * f4, f3, (-0.5F * f4)).setColor(175, 100, 215, 0);
                vb.addVertex(pose, 0.0F, f3, (1.0F * f4)).setColor(175, 100, 215, 0);
                vb.addVertex(pose, -0.866F * f4, f3, (-0.5F * f4)).setColor(175, 100, 215, 0);
            });
        }
    }

    private void renderPortalBase(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, float progress) {
        poseStack.pushPose();

        poseStack.mulPose(cameraRenderState.orientation);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        renderIconForProgress(poseStack, submitNodeCollector, ((int) (progress * 100)) % 4, progress);

        poseStack.popPose();
    }

    private void renderIconForProgress(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int index, float progress) {
        if(progress > 0.8F) {
            progress -= (progress - 0.8F) * 4;
        }

        float u1 = .0625f * index;
        float u2 = .0625f * (index + 1);
        float v1 = 0;
        float v2 = .0625f;

        poseStack.pushPose();
        poseStack.scale(0.5f * progress, 0.5f * progress, 0.5f * progress);
        poseStack.translate(-0.5F, -0.5f, 0);

        int j = 150;
        int k = 150;
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.text(PORTALBASE), (pose, vb) -> {
            vb.addVertex(pose, 0, 1, 0).setColor(0.72F, 0.5f, 0.23F, 0.9F).setUv(u1, v2).setUv2(j, k);
            vb.addVertex(pose, 0, 0, 0).setColor(0.72F, 0.5f, 0.83F, 0.9F).setUv(u1, v1).setUv2(j, k);
            vb.addVertex(pose, 1, 0, 0).setColor(0.72F, 0.5f, 0.83F, 0.9F).setUv(u2, v1).setUv2(j, k);
            vb.addVertex(pose, 1, 1, 0).setColor(0.72F, 0.5f, 0.83F, 0.9F).setUv(u2, v2).setUv2(j, k);
        });

        poseStack.popPose();
    }

    public static class RenderState extends BlockEntityRenderState {
        public float progress;
        public float partialTicks;
    }

}
