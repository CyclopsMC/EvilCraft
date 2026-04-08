package org.cyclops.evilcraft.client.render.blockentity;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.evilcraft.api.broom.IBroom;
import org.cyclops.evilcraft.block.BlockDisplayStand;
import org.cyclops.evilcraft.blockentity.BlockEntityDisplayStand;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Map;

/**
 * Renderer for the item inside the {@link BlockDisplayStand}.
 *
 * @author rubensworks
 *
 */
public class RenderBlockEntityDisplayStand implements BlockEntityRenderer<BlockEntityDisplayStand, RenderBlockEntityDisplayStand.RenderState> {

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
    public RenderState createRenderState() {
        return new RenderState();
    }

    @Override
    public void extractRenderState(BlockEntityDisplayStand blockEntity, RenderState renderState, float partialTick, Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        renderState.item = blockEntity.getInventory().getItem(0);
        renderState.level = blockEntity.getLevel();
        renderState.positiveDirection = blockEntity.getDirection() == Direction.AxisDirection.POSITIVE;
        if (blockEntity.getLevel() != null) {
            renderState.blockState = blockEntity.getLevel().getBlockState(blockEntity.getBlockPos());
        }
    }

    @Override
    public void submit(RenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        if(!renderState.item.isEmpty()) {
            submitItem(poseStack, submitNodeCollector, renderState.level, renderState.item,
                    IModHelpers.get().getBlockHelpers().getSafeBlockStateProperty(renderState.blockState, BlockDisplayStand.FACING, Direction.NORTH),
                    IModHelpers.get().getBlockHelpers().getSafeBlockStateProperty(renderState.blockState, BlockDisplayStand.AXIS_X, true),
                    renderState.positiveDirection);
        }
    }

    private void submitItem(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, Level level, ItemStack itemStack, Direction facing, boolean axisX, boolean positiveDirection) {
        poseStack.pushPose();

        ItemStackRenderState renderState = new ItemStackRenderState();
        Minecraft.getInstance().getItemModelResolver().updateForTopItem(renderState, itemStack, ItemDisplayContext.FIXED, level, null, 0);

        poseStack.translate(0.5F, 0.5F, 0.5F);
        if (itemStack.getItem() instanceof BlockItem) {
            poseStack.scale(0.6F, 0.6F, 0.6F);
            poseStack.mulPose(Axis.YP.rotationDegrees(90F));
        } else if (itemStack.getItem() instanceof IBroom) {
            poseStack.scale(2F, 2F, 2F);
        } else if (!(itemStack.getItem() instanceof IBroom)) {
            poseStack.scale(0.5F, 0.5F, 0.5F);
            poseStack.translate(0F, 0.25F, 0F);
            poseStack.mulPose(Axis.YP.rotationDegrees(90F));
        }

        Vector3f vec = ROTATIONS.get(facing);
        poseStack.mulPose(Axis.XP.rotationDegrees(vec.x()));
        poseStack.mulPose(Axis.YP.rotationDegrees(vec.y()));

        if (!axisX) {
            poseStack.mulPose(Axis.YP.rotationDegrees(90F));
            if (!positiveDirection) {
                poseStack.mulPose(Axis.YP.rotationDegrees(180F));
            }
        } else {
            if (positiveDirection) {
                poseStack.mulPose(Axis.YP.rotationDegrees(180F));
            }
        }

        renderState.submit(poseStack, submitNodeCollector, 15728880, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }

    public static class RenderState extends BlockEntityRenderState {
        public ItemStack item;
        public Level level;
        public boolean positiveDirection;
        public net.minecraft.world.level.block.state.BlockState blockState;
    }

}
