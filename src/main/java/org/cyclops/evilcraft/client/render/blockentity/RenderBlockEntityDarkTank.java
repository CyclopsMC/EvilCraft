package org.cyclops.evilcraft.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.helper.DirectionHelpers;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.evilcraft.blockentity.BlockEntityDarkTank;
import org.cyclops.evilcraft.client.render.model.ModelDarkTankBaked;
import org.joml.Matrix4f;

/**
 * Renderer for the {@link org.cyclops.evilcraft.block.BlockDarkTank}.
 * @author rubensworks
 */
public class RenderBlockEntityDarkTank implements BlockEntityRenderer<BlockEntityDarkTank> {

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
    public void render(final BlockEntityDarkTank tile, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        FluidStack fluid = tile.getTank().getFluid();
        RenderHelpers.renderFluidContext(fluid, matrixStackIn, () -> {
            float height = (float) (tile.getFillRatio() * 0.99F);
            int brightness = Math.max(combinedLightIn, fluid.getFluid().getFluidType().getLightLevel(fluid));
            renderFluidSides(height, tile.getTank().getFluid(), tile.isEnabled(), brightness, matrixStackIn, bufferIn);
        });
    }

    public static void renderFluidSides(float height, FluidStack fluid, boolean flowing, int brightness, PoseStack matrixStackIn, MultiBufferSource bufferIn) {
        int l2 = brightness >> 0x10 & 0xFFFF;
        int i3 = brightness & 0xFFFF;
        Triple<Float, Float, Float> colorParts = RenderHelpers.getFluidVertexBufferColor(fluid);
        float r = colorParts.getLeft();
        float g = colorParts.getMiddle();
        float b = colorParts.getRight();
        float a = 1.0F;
        Matrix4f matrix = matrixStackIn.last().pose();
        for (Direction side : DirectionHelpers.DIRECTIONS) {
            TextureAtlasSprite icon = ModelDarkTankBaked.getFluidIcon(fluid, flowing, side);
            VertexConsumer vb = bufferIn.getBuffer(RenderType.text(icon.atlasLocation()));
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
            vb.addVertex(matrix, c[0][0], getHeight(side, c[0][1], height), c[0][2]).setColor(r, g, b, a).setUv(minU, maxV).setUv2(l2, i3);
            vb.addVertex(matrix, c[1][0], getHeight(side, c[1][1], height), c[1][2]).setColor(r, g, b, a).setUv(minU, minV).setUv2(l2, i3);
            vb.addVertex(matrix, c[2][0], getHeight(side, c[2][1], height), c[2][2]).setColor(r, g, b, a).setUv(maxU, minV).setUv2(l2, i3);
            vb.addVertex(matrix, c[3][0], getHeight(side, c[3][1], height), c[3][2]).setColor(r, g, b, a).setUv(maxU, maxV).setUv2(l2, i3);
        }
    }

    private static float getHeight(Direction side, float height, float replaceHeight) {
        if(height == MAX) {
            return replaceHeight;
        }
        return height;
    }

}
