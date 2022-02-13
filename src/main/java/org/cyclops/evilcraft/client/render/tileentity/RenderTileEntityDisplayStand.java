package org.cyclops.evilcraft.client.render.tileentity;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.evilcraft.api.broom.IBroom;
import org.cyclops.evilcraft.block.BlockDisplayStand;
import org.cyclops.evilcraft.tileentity.TileDisplayStand;

import java.util.Map;

/**
 * Renderer for the item inside the {@link BlockDisplayStand}.
 * 
 * @author rubensworks
 *
 */
public class RenderTileEntityDisplayStand extends TileEntityRenderer<TileDisplayStand> {

    private static final Map<Direction, Vector3f> ROTATIONS = ImmutableMap.<Direction, Vector3f>builder()
            .put(Direction.NORTH, new Vector3f(270, 0, 0))
            .put(Direction.SOUTH, new Vector3f(90, 0, 0))
            .put(Direction.WEST, new Vector3f(0, 90, 0))
            .put(Direction.EAST, new Vector3f(0, 90, 0))
            .put(Direction.UP, new Vector3f(180, 180, 0))
            .put(Direction.DOWN, new Vector3f(0, 0, 0))
            .build();

    public RenderTileEntityDisplayStand(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
	public void render(TileDisplayStand tile, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if(!tile.getInventory().getItem(0).isEmpty()) {
            BlockState blockState = tile.getLevel().getBlockState(tile.getBlockPos());
            renderItem(matrixStackIn, bufferIn, tile.getInventory().getItem(0),
                    BlockHelpers.getSafeBlockStateProperty(blockState, BlockDisplayStand.FACING, Direction.NORTH),
                    BlockHelpers.getSafeBlockStateProperty(blockState, BlockDisplayStand.AXIS_X, true),
                    tile.getDirection() == Direction.AxisDirection.POSITIVE);
        }
	}
	
	private void renderItem(MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, ItemStack itemStack, Direction facing, boolean axisX, boolean positiveDirection) {
        matrixStack.pushPose();

        matrixStack.translate(0.5F, 0.5F, 0.5F);
        if (itemStack.getItem() instanceof BlockItem) {
            matrixStack.scale(0.6F, 0.6F, 0.6F);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(90F));
        } else if (itemStack.getItem() instanceof IBroom) {
            matrixStack.scale(2F, 2F, 2F);
        } else if (!(itemStack.getItem() instanceof IBroom)) {
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            matrixStack.translate(0F, 0.25F, 0F);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(90F));
        }

        Vector3f vec = ROTATIONS.get(facing);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(vec.x()));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(vec.y()));

        if (!axisX) {
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(90F));
            if (!positiveDirection) {
                matrixStack.mulPose(Vector3f.YP.rotationDegrees(180F));
            }
        } else {
            if (positiveDirection) {
                matrixStack.mulPose(Vector3f.YP.rotationDegrees(180F));
            }
        }

        Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemCameraTransforms.TransformType.FIXED, 15728880, OverlayTexture.NO_OVERLAY, matrixStack, renderTypeBuffer);
        matrixStack.popPose();
    }

}
