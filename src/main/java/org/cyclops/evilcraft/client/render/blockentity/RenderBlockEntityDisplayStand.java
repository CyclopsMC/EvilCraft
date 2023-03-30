package org.cyclops.evilcraft.client.render.blockentity;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.evilcraft.api.broom.IBroom;
import org.cyclops.evilcraft.block.BlockDisplayStand;
import org.cyclops.evilcraft.blockentity.BlockEntityDisplayStand;
import org.joml.Vector3f;

import java.util.Map;

/**
 * Renderer for the item inside the {@link BlockDisplayStand}.
 *
 * @author rubensworks
 *
 */
public class RenderBlockEntityDisplayStand implements BlockEntityRenderer<BlockEntityDisplayStand> {

    private static final Map<Direction, Vector3f> ROTATIONS = ImmutableMap.<Direction, Vector3f>builder()
            .put(Direction.NORTH, new Vector3f(270, 0, 0))
            .put(Direction.SOUTH, new Vector3f(90, 0, 0))
            .put(Direction.WEST, new Vector3f(0, 90, 0))
            .put(Direction.EAST, new Vector3f(0, 90, 0))
            .put(Direction.UP, new Vector3f(180, 180, 0))
            .put(Direction.DOWN, new Vector3f(0, 0, 0))
            .build();

    public RenderBlockEntityDisplayStand(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(BlockEntityDisplayStand tile, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if(!tile.getInventory().getItem(0).isEmpty()) {
            BlockState blockState = tile.getLevel().getBlockState(tile.getBlockPos());
            renderItem(matrixStackIn, bufferIn, tile.getInventory().getItem(0),
                    BlockHelpers.getSafeBlockStateProperty(blockState, BlockDisplayStand.FACING, Direction.NORTH),
                    BlockHelpers.getSafeBlockStateProperty(blockState, BlockDisplayStand.AXIS_X, true),
                    tile.getDirection() == Direction.AxisDirection.POSITIVE, tile.getLevel());
        }
    }

    private void renderItem(PoseStack matrixStack, MultiBufferSource renderTypeBuffer, ItemStack itemStack, Direction facing, boolean axisX, boolean positiveDirection, Level level) {
        matrixStack.pushPose();

        matrixStack.translate(0.5F, 0.5F, 0.5F);
        if (itemStack.getItem() instanceof BlockItem) {
            matrixStack.scale(0.6F, 0.6F, 0.6F);
            matrixStack.mulPose(Axis.YP.rotationDegrees(90F));
        } else if (itemStack.getItem() instanceof IBroom) {
            matrixStack.scale(2F, 2F, 2F);
        } else if (!(itemStack.getItem() instanceof IBroom)) {
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            matrixStack.translate(0F, 0.25F, 0F);
            matrixStack.mulPose(Axis.YP.rotationDegrees(90F));
        }

        Vector3f vec = ROTATIONS.get(facing);
        matrixStack.mulPose(Axis.XP.rotationDegrees(vec.x()));
        matrixStack.mulPose(Axis.YP.rotationDegrees(vec.y()));

        if (!axisX) {
            matrixStack.mulPose(Axis.YP.rotationDegrees(90F));
            if (!positiveDirection) {
                matrixStack.mulPose(Axis.YP.rotationDegrees(180F));
            }
        } else {
            if (positiveDirection) {
                matrixStack.mulPose(Axis.YP.rotationDegrees(180F));
            }
        }

        Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemDisplayContext.FIXED, 15728880, OverlayTexture.NO_OVERLAY, matrixStack, renderTypeBuffer, level, 0);
        matrixStack.popPose();
    }

}
