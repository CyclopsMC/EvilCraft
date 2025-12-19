package org.cyclops.evilcraft.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.chest.ChestModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * A modified copy of {@link ChestRenderer}.
 * @author rubensworks
 */
public abstract class RenderBlockEntityChestBase<T extends BlockEntity & LidBlockEntity, S extends RenderBlockEntityChestBase.RenderState> implements BlockEntityRenderer<T, S> {

    private final ChestModel singleModel;
    private final boolean xmasTextures = ChestRenderer.xmasTextures();
    protected final MaterialSet materials;

    public RenderBlockEntityChestBase(BlockEntityRendererProvider.Context context) {
        this.singleModel = new ChestModel(context.bakeLayer(ModelLayers.CHEST));
        this.materials = context.materials();
    }

    protected abstract Direction getDirection(S renderState);

    protected Material getMaterial(S renderState) {
        return Sheets.chooseMaterial(renderState.chestMaterialType, ChestType.SINGLE);
    }

    protected void handleRotation(S renderState, PoseStack matrixStack) {
        float f = getDirection(renderState).toYRot();
        matrixStack.mulPose(Axis.YP.rotationDegrees(-f));
    }

    @Override
    public void extractRenderState(T blockEntity, S renderState, float partialTick, Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        renderState.chestMaterialType = this.xmasTextures ? ChestRenderState.ChestMaterialType.CHRISTMAS : ChestRenderState.ChestMaterialType.REGULAR;
        renderState.openNess = blockEntity.getOpenNess(partialTick);
        renderState.openNessRaw = blockEntity.getOpenNess(0);
    }

    @Override
    public void submit(S renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        handleRotation(renderState, poseStack);
        poseStack.translate(-0.5D, -0.5D, -0.5D);

        float f1 = renderState.openNess;
        Material material = this.getMaterial(renderState);
        TextureAtlasSprite textureAtlasSprite = this.materials.get(material);
        submitNodeCollector.submitModel(this.singleModel, f1, poseStack, material.renderType(RenderTypes::entityCutout), renderState.lightCoords, OverlayTexture.NO_OVERLAY, -1, textureAtlasSprite, 0, renderState.breakProgress);

        poseStack.popPose();
    }

    public static class RenderState extends BlockEntityRenderState {
        public ChestRenderState.ChestMaterialType chestMaterialType;
        public float openNess;
        public float openNessRaw;
    }

}
