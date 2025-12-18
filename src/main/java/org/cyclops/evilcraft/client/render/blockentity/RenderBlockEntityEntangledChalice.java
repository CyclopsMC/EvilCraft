package org.cyclops.evilcraft.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;
import org.cyclops.evilcraft.block.BlockEntangledChalice;
import org.cyclops.evilcraft.blockentity.BlockEntityEntangledChalice;
import org.jetbrains.annotations.Nullable;

/**
 * Renderer for the {@link BlockEntangledChalice}.
 * @author rubensworks
 *
 */
public class RenderBlockEntityEntangledChalice implements BlockEntityRenderer<BlockEntityEntangledChalice, RenderBlockEntityEntangledChalice.RenderState> {

    public RenderBlockEntityEntangledChalice(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public RenderState createRenderState() {
        return new RenderState();
    }

    @Override
    public void extractRenderState(BlockEntityEntangledChalice blockEntity, RenderState renderState, float partialTick, Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        renderState.fluid = blockEntity.getTank().getFluid();
        renderState.capacity = blockEntity.getTank().getCapacity();
        renderState.color = IModHelpersNeoForge.get().getRenderHelpers().getFluidVertexBufferColor(renderState.fluid);
    }

    @Override
    public void submit(RenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        if(!renderState.fluid.isEmpty()) {
            FluidStack fluid = renderState.fluid;
            IModHelpersNeoForge.get().getRenderHelpers().renderFluidContext(renderState.fluid, poseStack, () -> {
                float height = Math.min(0.95F, ((float) fluid.getAmount() / (float) renderState.capacity)) * 0.1875F + 0.8125F;
                int brightness = Math.max(renderState.lightCoords, fluid.getFluid().getFluidType().getLightLevel(fluid));
                int l2 = brightness >> 0x10 & 0xFFFF;
                int i3 = brightness & 0xFFFF;

                TextureAtlasSprite icon = IModHelpersNeoForge.get().getRenderHelpers().getFluidIcon(fluid, Direction.UP);
                Triple<Float, Float, Float> color = renderState.color;

                submitNodeCollector.submitCustomGeometry(poseStack, RenderType.text(icon.atlasLocation()), (pose, vb) -> {
                    vb.addVertex(pose, 0.1875F, height, 0.1875F).setColor(color.getLeft(), color.getMiddle(), color.getRight(), 1).setUv(icon.getU0(), icon.getV1()).setUv2(l2, i3);
                    vb.addVertex(pose, 0.1875F, height, 0.8125F).setColor(color.getLeft(), color.getMiddle(), color.getRight(), 1).setUv(icon.getU0(), icon.getV0()).setUv2(l2, i3);
                    vb.addVertex(pose, 0.8125F, height, 0.8125F).setColor(color.getLeft(), color.getMiddle(), color.getRight(), 1).setUv(icon.getU1(), icon.getV0()).setUv2(l2, i3);
                    vb.addVertex(pose, 0.8125F, height, 0.1875F).setColor(color.getLeft(), color.getMiddle(), color.getRight(), 1).setUv(icon.getU1(), icon.getV1()).setUv2(l2, i3);
                });
            });
        }
    }

    @Override
    public boolean shouldRender(BlockEntityEntangledChalice blockEntity, Vec3 cameraPos) {
        return blockEntity.getBlockPos() == BlockPos.ZERO || BlockEntityRenderer.super.shouldRender(blockEntity, cameraPos);
    }

    public static class RenderState extends BlockEntityRenderState {
        public FluidStack fluid;
        public int capacity;
        public Triple<Float, Float, Float> color;
    }

}
