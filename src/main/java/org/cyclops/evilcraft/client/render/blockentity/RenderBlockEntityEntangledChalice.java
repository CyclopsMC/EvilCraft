package org.cyclops.evilcraft.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.evilcraft.block.BlockEntangledChalice;
import org.cyclops.evilcraft.blockentity.BlockEntityEntangledChalice;
import org.joml.Matrix4f;

/**
 * Renderer for the {@link BlockEntangledChalice}.
 * @author rubensworks
 *
 */
public class RenderBlockEntityEntangledChalice implements BlockEntityRenderer<BlockEntityEntangledChalice> {

    public RenderBlockEntityEntangledChalice(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(final BlockEntityEntangledChalice tile, float partialTicks, PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int combinedLight, int combinedOverlayIn) {
        if(tile != null && !tile.getTank().getFluid().isEmpty() && tile.getTank().getFluid().getFluid() != null) {
            try {
                FluidStack fluid = tile.getTank().getFluid();
                RenderHelpers.renderFluidContext(tile.getTank().getFluid(), matrixStack, () -> {
                    float height = Math.min(0.95F, ((float) fluid.getAmount() / (float) tile.getTank().getCapacity())) * 0.1875F + 0.8125F;
                    IClientFluidTypeExtensions renderProperties = IClientFluidTypeExtensions.of(fluid.getFluid());
                    int brightness = Math.max(combinedLight, fluid.getFluid().getFluidType().getLightLevel(fluid));
                    int l2 = brightness >> 0x10 & 0xFFFF;
                    int i3 = brightness & 0xFFFF;

                    TextureAtlasSprite icon = RenderHelpers.getFluidIcon(fluid, Direction.UP);
                    Triple<Float, Float, Float> color = Helpers.intToRGB(renderProperties.getTintColor(fluid.getFluid().defaultFluidState(), tile.getLevel(), tile.getBlockPos()));

                    VertexConsumer vb = renderTypeBuffer.getBuffer(RenderType.text(icon.atlasLocation()));
                    Matrix4f matrix = matrixStack.last().pose();
                    vb.vertex(matrix, 0.1875F, height, 0.1875F).color(color.getLeft(), color.getMiddle(), color.getRight(), 1).uv(icon.getU0(), icon.getV1()).uv2(l2, i3).endVertex();
                    vb.vertex(matrix, 0.1875F, height, 0.8125F).color(color.getLeft(), color.getMiddle(), color.getRight(), 1).uv(icon.getU0(), icon.getV0()).uv2(l2, i3).endVertex();
                    vb.vertex(matrix, 0.8125F, height, 0.8125F).color(color.getLeft(), color.getMiddle(), color.getRight(), 1).uv(icon.getU1(), icon.getV0()).uv2(l2, i3).endVertex();
                    vb.vertex(matrix, 0.8125F, height, 0.1875F).color(color.getLeft(), color.getMiddle(), color.getRight(), 1).uv(icon.getU1(), icon.getV1()).uv2(l2, i3).endVertex();
                });
            } catch (NullPointerException e) {
                // This can happen because worlds are multi-threaded, and the fluid suddenly may become null while out fluid context is being executed.
                // Unfortunately we can't lock on worlds, so there isn't really a cleaner solution for this.
            }
        }
    }

}
