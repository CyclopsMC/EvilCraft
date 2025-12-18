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
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.helper.DirectionHelpers;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;
import org.cyclops.evilcraft.blockentity.BlockEntityDarkTank;
import org.cyclops.evilcraft.client.render.model.ModelDarkTankBaked;
import org.jetbrains.annotations.Nullable;

/**
 * Renderer for the {@link org.cyclops.evilcraft.block.BlockDarkTank}.
 * @author rubensworks
 */
public class RenderBlockEntityDarkTank implements BlockEntityRenderer<BlockEntityDarkTank, RenderBlockEntityDarkTank.RenderState> {

    private static final float OFFSET = 0.01F;
    private static final float MINY = OFFSET;
    private static final float MIN = 0.125F + OFFSET;
    private static final float MAX = 0.875F - OFFSET;
    private static float[][][] coordinates = {
            { // DOWN
                    {MIN, MINY, MIN},
                    {MIN, MINY, MAX},
                    {MAX, MINY, MAX},
                    {MAX, MINY, MIN}
            },
            { // UP
                    {MIN, MAX, MIN},
                    {MIN, MAX, MAX},
                    {MAX, MAX, MAX},
                    {MAX, MAX, MIN}
            },
            { // NORTH
                    {MIN, MINY, MIN},
                    {MIN, MAX, MIN},
                    {MAX, MAX, MIN},
                    {MAX, MINY, MIN}
            },
            { // SOUTH
                    {MIN, MAX, MAX},
                    {MIN, MINY, MAX},
                    {MAX, MINY, MAX},
                    {MAX, MAX, MAX}

            },
            { // WEST
                    {MIN, MAX, MIN},
                    {MIN, MINY, MIN},
                    {MIN, MINY, MAX},
                    {MIN, MAX, MAX}

            },
            { // EAST
                    {MAX, MINY, MIN},
                    {MAX, MAX, MIN},
                    {MAX, MAX, MAX},
                    {MAX, MINY, MAX}
            }
    };

    public RenderBlockEntityDarkTank(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public RenderState createRenderState() {
        return new RenderState();
    }

    @Override
    public void extractRenderState(BlockEntityDarkTank blockEntity, RenderState renderState, float partialTick, Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        renderState.fluid = blockEntity.getTank().getFluid();
        renderState.enabled = blockEntity.isEnabled();
        renderState.fillRatio = blockEntity.getFillRatio();
    }

    @Override
    public void submit(RenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        FluidStack fluid = renderState.fluid;
        IModHelpersNeoForge.get().getRenderHelpers().renderFluidContext(fluid, poseStack, () -> {
            float height = (float) (renderState.fillRatio * 0.99F);
            int brightness = Math.max(renderState.lightCoords, fluid.getFluid().getFluidType().getLightLevel(fluid));
            renderFluidSides(height, renderState.fluid, renderState.enabled, brightness, poseStack, submitNodeCollector);
        });
    }

    public static void renderFluidSides(float height, FluidStack fluid, boolean flowing, int brightness, PoseStack matrixStackIn, SubmitNodeCollector submitNodeCollector) {
        int l2 = brightness >> 0x10 & 0xFFFF;
        int i3 = brightness & 0xFFFF;
        Triple<Float, Float, Float> colorParts = IModHelpersNeoForge.get().getRenderHelpers().getFluidVertexBufferColor(fluid);
        float r = colorParts.getLeft();
        float g = colorParts.getMiddle();
        float b = colorParts.getRight();
        float a = 1.0F;
        for (Direction side : DirectionHelpers.DIRECTIONS) {
            TextureAtlasSprite icon = ModelDarkTankBaked.getFluidIcon(fluid, flowing, side);
            submitNodeCollector.submitCustomGeometry(matrixStackIn, RenderType.text(icon.atlasLocation()), (pose, vb) -> {
                float[][] c = coordinates[side.ordinal()];
                float minV = icon.getV0();
                float maxV = (icon.getV1() - icon.getV0()) * height + icon.getV0();
                float minU = icon.getU0();
                float maxU = icon.getU1();
                if (side == Direction.WEST || side == Direction.SOUTH) {
                    // Flip up-side down
                    float tmp = minV;
                    minV = maxV;
                    maxV = tmp;
                } else if (side == Direction.UP || side == Direction.DOWN) {
                    maxV = icon.getV1();
                }
                vb.addVertex(pose, c[0][0], getHeight(side, c[0][1], height), c[0][2]).setColor(r, g, b, a).setUv(minU, maxV).setUv2(l2, i3);
                vb.addVertex(pose, c[1][0], getHeight(side, c[1][1], height), c[1][2]).setColor(r, g, b, a).setUv(minU, minV).setUv2(l2, i3);
                vb.addVertex(pose, c[2][0], getHeight(side, c[2][1], height), c[2][2]).setColor(r, g, b, a).setUv(maxU, minV).setUv2(l2, i3);
                vb.addVertex(pose, c[3][0], getHeight(side, c[3][1], height), c[3][2]).setColor(r, g, b, a).setUv(maxU, maxV).setUv2(l2, i3);
            });
        }
    }

    private static float getHeight(Direction side, float height, float replaceHeight) {
        if(height == MAX) {
            return replaceHeight;
        }
        return height;
    }

    public static class RenderState extends BlockEntityRenderState {
        public FluidStack fluid;
        public boolean enabled;
        public double fillRatio;
    }

}
