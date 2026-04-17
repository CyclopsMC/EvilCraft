package org.cyclops.evilcraft.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.blockentity.BlockEntityBoxOfEternalClosure;
import org.cyclops.evilcraft.client.render.model.ModelBoxOfEternalClosureBaked;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpirit;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

/**
 * Renderer for the {@link org.cyclops.evilcraft.block.BlockBoxOfEternalClosure}.
 * @author rubensworks
 *
 */
public class RenderBlockEntityBoxOfEternalClosure extends RendererBlockEntityEndPortalBase<BlockEntityBoxOfEternalClosure, RenderBlockEntityBoxOfEternalClosure.RenderState> {

    private static final Identifier beamTexture =
            Identifier.fromNamespaceAndPath(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITIES + "beam.png");
    private static final RenderType renderTypeBeam = RenderTypes.entityCutout(beamTexture);

    public RenderBlockEntityBoxOfEternalClosure(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public boolean shouldRender(BlockEntityBoxOfEternalClosure blockEntity, Vec3 cameraPos) {
        return blockEntity.getBlockPos() == BlockPos.ZERO || super.shouldRender(blockEntity, cameraPos);
    }

    @Override
    public AABB getRenderBoundingBox(BlockEntityBoxOfEternalClosure blockEntity) {
        return AABB.INFINITE;
    }

    @Override
    public RenderState createRenderState() {
        return new RenderState();
    }

    @Override
    public void extractRenderState(BlockEntityBoxOfEternalClosure blockEntity, RenderState renderState, float partialTick, Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        renderState.previousLidAngle = blockEntity.getPreviousLidAngle();
        renderState.lidAngle = blockEntity.getLidAngle();
        renderState.partialTicks = partialTick;
        renderState.crystalBeamOffsetY = getY(blockEntity, renderState.partialTicks);
        EntityVengeanceSpirit targetSpirit = blockEntity.getTargetSpirit();
        renderState.hasTarget = targetSpirit != null;
        if (renderState.hasTarget) {
            renderState.target = targetSpirit.position();
            renderState.eyeHeight = targetSpirit.getEyeHeight();
        }
        if (blockEntity.getLevel() != null) {
            renderState.blockState = blockEntity.getLevel().getBlockState(blockEntity.getBlockPos());
        }
    }

    @Override
    public void submit(RenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        Direction direction = IModHelpers.get().getBlockHelpers().getSafeBlockStateProperty(
                renderState.blockState, org.cyclops.evilcraft.block.BlockBoxOfEternalClosure.FACING, Direction.NORTH);

        poseStack.pushPose();
        short rotation = 0;
        if (direction == Direction.SOUTH) {
            rotation = -90;
        }
        if (direction == Direction.NORTH) {
            rotation = 90;
        }
        if (direction == Direction.WEST) {
            rotation = 180;
        }
        if (direction == Direction.EAST) {
            rotation = 0;
        }

        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        poseStack.translate(-0.5F, -0.5F, -0.5F);

        // Render box
        BlockState blockState = (renderState.blockState != null && renderState.blockState.getBlock() instanceof org.cyclops.evilcraft.block.BlockBoxOfEternalClosure)
                ? renderState.blockState.setValue(org.cyclops.evilcraft.block.BlockBoxOfEternalClosure.FACING, Direction.NORTH)
                : org.cyclops.evilcraft.RegistryEntries.BLOCK_BOX_OF_ETERNAL_CLOSURE.get().defaultBlockState().setValue(org.cyclops.evilcraft.block.BlockBoxOfEternalClosure.FACING, Direction.NORTH);
        ModelBoxOfEternalClosureBaked model = (ModelBoxOfEternalClosureBaked) IModHelpers.get().getRenderHelpers().getBakedModel(blockState);
        java.util.List<net.minecraft.client.renderer.block.dispatch.BlockStateModelPart> boxParts = new java.util.ArrayList<>();
        model.getBoxModel().collectParts(net.minecraft.util.RandomSource.create(), boxParts);
        submitNodeCollector.submitBlockModel(poseStack, Sheets.cutoutBlockSheet(), boxParts, net.minecraft.client.renderer.block.BlockModelRenderState.EMPTY_TINTS, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);

        // Render lid
        float angle = renderState.previousLidAngle
                + (renderState.lidAngle - renderState.previousLidAngle) * renderState.partialTicks;
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        poseStack.translate(-0.5F, -0.5F, -0.5F);
        poseStack.translate(0.25F, 0.375F, 0F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(angle));
        poseStack.translate(-0.25F, -0.375F, 0F);
        java.util.List<net.minecraft.client.renderer.block.dispatch.BlockStateModelPart> lidParts = new java.util.ArrayList<>();
        model.getBoxLidModel().collectParts(net.minecraft.util.RandomSource.create(), lidParts);
        submitNodeCollector.submitBlockModel(poseStack, Sheets.cutoutBlockSheet(), lidParts, net.minecraft.client.renderer.block.BlockModelRenderState.EMPTY_TINTS, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();

        // Render box inside
        if(angle > 0) {
            poseStack.pushPose();
            poseStack.translate(0F, 0.75F, 0F);
            super.submit(renderState, poseStack, submitNodeCollector, cameraRenderState);
            poseStack.popPose();
        }
        poseStack.popPose();

        // Optionally render beam
        // Copied from EndCrystalRenderer
        if (renderState.hasTarget) {
            BlockPos blockpos = renderState.blockPos;

            float f = renderState.crystalBeamOffsetY;
            float f1 = (float)renderState.target.x + 0.5F - blockpos.getX();
            float f2 = (float)renderState.target.y + 0.5F - (renderState.eyeHeight / 2) - blockpos.getY();
            float f3 = (float)renderState.target.z  + 0.5F - blockpos.getZ();
            poseStack.translate(0.5, -1.5, 0.5);
            EnderDragonRenderer.submitCrystalBeams(f1 - 0.5F, f2 - 0.5F - f, f3 - 0.5F, renderState.partialTicks, poseStack, submitNodeCollector, renderState.lightCoords);
        }
    }

    @Override
    public boolean shouldRenderFace(Direction direction) {
        return direction == Direction.UP;
    }

    public static float getY(BlockEntityBoxOfEternalClosure p_229051_0_, float p_229051_1_) {
        float f = (float)p_229051_0_.innerRotation + p_229051_1_;
        float f1 = Mth.sin(f * 0.2F) / 2.0F + 0.5F;
        f1 = (f1 * f1 + f1) * 0.4F;
        return f1 - 1.4F;
    }

    @Override
    public void renderCube(Matrix4f pose, VertexConsumer consumer) {
        this.renderFace(pose, consumer, 0.3125F, 1.0F - 0.3125F, -0.5F, -0.5F, 1.0F, 1.0F, 0.0F, 0.0F, Direction.UP);
    }

    public static class RenderState extends BlockEntityRenderState {
        public float previousLidAngle;
        public float lidAngle;
        public float partialTicks;
        public float crystalBeamOffsetY;
        public boolean hasTarget;
        public Vec3 target;
        public float eyeHeight;
        public net.minecraft.world.level.block.state.BlockState blockState;
    }

}
