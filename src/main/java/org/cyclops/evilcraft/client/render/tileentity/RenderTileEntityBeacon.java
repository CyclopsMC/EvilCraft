package org.cyclops.evilcraft.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraft.client.renderer.tileentity.BeaconTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import org.cyclops.evilcraft.core.tileentity.EvilCraftBeaconTileEntity;
import org.lwjgl.opengl.GL11;

/**
 * EvilCraft's version of a beacon renderer, this allows us to have custom colors
 * and customize other stuff without being dependend on vanilla code
 * 
 * @author immortaleeb
 *
 */
public abstract class RenderTileEntityBeacon<T extends EvilCraftBeaconTileEntity> extends TileEntityRenderer<T> {
	
	private static final ResourceLocation BEACON_TEXTURE = new ResourceLocation("textures/entity/beacon_beam.png");

    public RenderTileEntityBeacon(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
	public void render(T tileentity, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		renderBeacon(tileentity, partialTicks, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
	}
	
	protected void renderBeacon(T tile, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
        GlStateManager.disableFog();
		
        if (tile.isBeamActive())
        {
        	Vector4f beamColor = tile.getBeamColor();
            BeaconTileEntityRenderer.renderBeamSegment(matrixStackIn, bufferIn, BEACON_TEXTURE, partialTicks, 1.0F,
                    tile.getWorld().getGameTime(), 0, 256,
                    new float[]{beamColor.getX(), beamColor.getY(), beamColor.getZ()}, isInnerBeam(tile) ? 0 : 0.2F, 0.25F);
        }

        GlStateManager.enableFog();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.5F);
	}

    protected abstract boolean isInnerBeam(T tile);

    @Override
    public boolean isGlobalRenderer(T te) {
        return true;
    }
}
