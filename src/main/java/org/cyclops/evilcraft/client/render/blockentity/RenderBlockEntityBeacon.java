package org.cyclops.evilcraft.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.evilcraft.core.blockentity.BlockEntityBeacon;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;

/**
 * EvilCraft's version of a beacon renderer, this allows us to have custom colors
 * and customize other stuff without being dependend on vanilla code
 *
 * @author immortaleeb
 *
 */
public abstract class RenderBlockEntityBeacon<T extends BlockEntityBeacon, S extends RenderBlockEntityBeacon.RenderState> implements BlockEntityRenderer<T, S> {

    private static final Identifier BEACON_TEXTURE = Identifier.withDefaultNamespace("textures/entity/beacon_beam.png");

    public RenderBlockEntityBeacon(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public AABB getRenderBoundingBox(T blockEntity) {
        return AABB.INFINITE;
    }

    @Override
    public void extractRenderState(T blockEntity, S renderState, float partialTick, Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        renderState.isBeamActive = blockEntity.isBeamActive();
        renderState.beamColor = blockEntity.getBeamColor();
        renderState.isInnerBeam = isInnerBeam(blockEntity);
        renderState.animationTime = blockEntity.getLevel() != null ? (float)Math.floorMod(blockEntity.getLevel().getGameTime(), 40) + partialTick : 0.0F;
    }

    @Override
    public void submit(S renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        submitBeacon(renderState, 1.0F, poseStack, submitNodeCollector);
    }

    protected void submitBeacon(S renderState, float partialTicks, PoseStack matrixStackIn, SubmitNodeCollector submitNodeCollector) {
        if (renderState.isBeamActive) {
            Vector4f beamColor = renderState.beamColor;
            BeaconRenderer.submitBeaconBeam(matrixStackIn, submitNodeCollector, BEACON_TEXTURE, partialTicks, renderState.animationTime,
                    0, 256,
                    IModHelpers.get().getBaseHelpers().RGBToInt((int) (beamColor.x() * 256), (int) (beamColor.y() * 256), (int) (beamColor.z() * 256)), renderState.isInnerBeam ? 0 : 0.2F, 0.25F);
        }
    }

    protected abstract boolean isInnerBeam(T tile);

    @Override
    public boolean shouldRenderOffScreen() {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 256;
    }

    public static class RenderState extends BlockEntityRenderState {
        public boolean isBeamActive;
        public Vector4f beamColor;
        public boolean isInnerBeam;
        public float animationTime;
    }
}
