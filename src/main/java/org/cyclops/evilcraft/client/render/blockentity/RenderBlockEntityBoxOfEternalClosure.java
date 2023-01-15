package org.cyclops.evilcraft.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.blockentity.BlockEntityBoxOfEternalClosure;
import org.cyclops.evilcraft.client.render.model.ModelBoxOfEternalClosureBaked;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpirit;
import org.joml.Matrix4f;

/**
 * Renderer for the {@link org.cyclops.evilcraft.block.BlockBoxOfEternalClosure}.
 * @author rubensworks
 *
 */
public class RenderBlockEntityBoxOfEternalClosure extends RendererBlockEntityEndPortalBase<BlockEntityBoxOfEternalClosure> {

    private static final ResourceLocation beamTexture =
            new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITIES + "beam.png");
    private static final RenderType renderTypeBeam = RenderType.entitySmoothCutout(beamTexture);

    public RenderBlockEntityBoxOfEternalClosure(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(BlockEntityBoxOfEternalClosure tile, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Direction direction = BlockHelpers.getSafeBlockStateProperty(
                tile.getLevel().getBlockState(tile.getBlockPos()), org.cyclops.evilcraft.block.BlockBoxOfEternalClosure.FACING, Direction.NORTH);
        matrixStackIn.pushPose();
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

        matrixStackIn.translate(0.5F, 0.5F, 0.5F);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(rotation));
        matrixStackIn.translate(-0.5F, -0.5F, -0.5F);

        // Render box
        BlockState blockState = tile.getBlockState()
                .setValue(org.cyclops.evilcraft.block.BlockBoxOfEternalClosure.FACING, Direction.NORTH);
        Minecraft.getInstance().getBlockRenderer().getModelRenderer().
                renderModel(matrixStackIn.last(), bufferIn.getBuffer(Sheets.solidBlockSheet()), blockState, ModelBoxOfEternalClosureBaked.boxModel, 1.0F, 1.0F, 1.0F, combinedLightIn, OverlayTexture.NO_OVERLAY);

        // Render lid
        float angle = tile.getPreviousLidAngle()
                + (tile.getLidAngle() - tile.getPreviousLidAngle()) * partialTicks;
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.75F, 0.375F, 0F);
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(-angle));
        matrixStackIn.translate(-0.75F, -0.375F, 0F);
        Minecraft.getInstance().getBlockRenderer().getModelRenderer().
                renderModel(matrixStackIn.last(), bufferIn.getBuffer(Sheets.solidBlockSheet()), blockState, ModelBoxOfEternalClosureBaked.boxLidModel, 1.0F, 1.0F, 1.0F, combinedLightIn, OverlayTexture.NO_OVERLAY);
        matrixStackIn.popPose();

        // Render box inside
        if(angle > 0) {
            super.render(tile, partialTicks, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        }
        matrixStackIn.popPose();

        // Optionally render beam
        // Copied from EnderCrystalRenderer
        EntityVengeanceSpirit target = tile.getTargetSpirit();
        if(target != null) {
            float f = getY(tile, partialTicks);
            BlockPos blockpos = tile.getBlockPos();

            float f3 = (float)target.getX() + 0.5F;
            float f4 = (float)target.getY() + 0.5F - (target.getEyeHeight() / 2);
            float f5 = (float)target.getZ()  + 0.5F;
            float f6 = (float)((double)f3 - blockpos.getX());
            float f7 = (float)((double)f4 - blockpos.getY());
            float f8 = (float)((double)f5 - blockpos.getZ());

            matrixStackIn.translate(f6, f7, f8);
            MultiBufferSource bufferOverride = (type) -> bufferIn.getBuffer(renderTypeBeam);
            EnderDragonRenderer.renderCrystalBeams(-f6, -f7 + f, -f8, partialTicks, tile.innerRotation, matrixStackIn, bufferOverride, combinedLightIn);
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
    public void renderCube(BlockEntityBoxOfEternalClosure tile, Matrix4f p_228883_4_, VertexConsumer vb) {
        this.renderFace(tile, p_228883_4_, vb, 0.3125F, 1.0F - 0.3125F, -0.5F, -0.5F, 1.0F, 1.0F, 0.0F, 0.0F, Direction.UP);
    }

}
