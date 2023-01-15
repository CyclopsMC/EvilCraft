package org.cyclops.evilcraft.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.cyclops.evilcraft.core.blockentity.BlockEntityBeacon;
import org.joml.Vector4f;

/**
 * EvilCraft's version of a beacon renderer, this allows us to have custom colors
 * and customize other stuff without being dependend on vanilla code
 *
 * @author immortaleeb
 *
 */
public abstract class RenderBlockEntityBeacon<T extends BlockEntityBeacon> implements BlockEntityRenderer<T> {

    private static final ResourceLocation BEACON_TEXTURE = new ResourceLocation("textures/entity/beacon_beam.png");

    public RenderBlockEntityBeacon(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(T tileentity, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        renderBeacon(tileentity, partialTicks, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
    }

    protected void renderBeacon(T tile, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (tile.isBeamActive()) {
            Vector4f beamColor = tile.getBeamColor();
            BeaconRenderer.renderBeaconBeam(matrixStackIn, bufferIn, BEACON_TEXTURE, partialTicks, 1.0F,
                    tile.getLevel().getGameTime(), 0, 256,
                    new float[]{beamColor.x(), beamColor.y(), beamColor.z()}, isInnerBeam(tile) ? 0 : 0.2F, 0.25F);
        }
    }

    protected abstract boolean isInnerBeam(T tile);

    @Override
    public boolean shouldRenderOffScreen(T te) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 256;
    }
}
