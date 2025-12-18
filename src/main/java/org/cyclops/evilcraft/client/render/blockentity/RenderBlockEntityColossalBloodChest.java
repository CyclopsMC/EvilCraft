package org.cyclops.evilcraft.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.BlockColossalBloodChest;
import org.cyclops.evilcraft.blockentity.BlockEntityColossalBloodChest;
import org.jetbrains.annotations.Nullable;

/**
 * Renderer for the {@link BlockColossalBloodChest}.
 * @author rubensworks
 *
 */
public class RenderBlockEntityColossalBloodChest extends RenderBlockEntityChestBase<BlockEntityColossalBloodChest, RenderBlockEntityColossalBloodChest.RenderState> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "model/colossal_blood_chest");

    public RenderBlockEntityColossalBloodChest(BlockEntityRendererProvider.Context p_i226008_1_) {
        super(p_i226008_1_);
    }

    @Override
    protected Direction getDirection(RenderState renderState) {
        return renderState.rotation;
    }

    @Override
    public AABB getRenderBoundingBox(BlockEntityColossalBloodChest blockEntity) {
        return new AABB(
                Vec3.atLowerCornerOf(blockEntity.getBlockPos().subtract(new Vec3i(3, 3, 3))),
                Vec3.atLowerCornerOf(blockEntity.getBlockPos().offset(3, 6, 3))
        );
    }

    @Override
    public RenderState createRenderState() {
        return new RenderState();
    }

    @Override
    public void extractRenderState(BlockEntityColossalBloodChest blockEntity, RenderState renderState, float partialTick, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        renderState.rotation = blockEntity.getRotation().getOpposite();
        renderState.isStructureComplete = blockEntity.isStructureComplete();
        renderState.renderOffset = blockEntity.getRenderOffset();
        renderState.sizeSingular = blockEntity.getSizeSingular();
    }

    @Override
    public boolean shouldRenderOffScreen() {
        return true;
    }

    @Override
    protected void handleRotation(RenderState renderState, PoseStack matrixStack) {
        // Move origin to center of chest
        if(renderState.isStructureComplete) {
            Vec3i renderOffset = renderState.renderOffset;
            matrixStack.translate(-renderOffset.getX(), -renderOffset.getY(), -renderOffset.getZ());
        }

        // Rotate
        super.handleRotation(renderState, matrixStack);

        // Move chest slightly higher
        matrixStack.translate(0F, renderState.sizeSingular * 0.0625F, 0F);

        // Scale
        float size = renderState.sizeSingular * 1.125F;
        matrixStack.scale(size, size, size);
    }

    @Override
    public void submit(RenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        if (renderState.isStructureComplete) {
            super.submit(renderState, poseStack, submitNodeCollector, cameraRenderState);
        }
    }

    @Override
    protected Material getMaterial(RenderState renderState) {
        return new Material(Sheets.CHEST_SHEET, TEXTURE);
    }

    public static class RenderState extends RenderBlockEntityChestBase.RenderState {
        public Direction rotation;
        public boolean isStructureComplete;
        public Vec3i renderOffset;
        public int sizeSingular;
    }
}
