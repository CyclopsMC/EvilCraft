package org.cyclops.evilcraft.client.render.tileentity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.cyclops.evilcraft.tileentity.EvilCraftBeaconTileEntity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;

/**
 * EvilCraft's version of a beacon renderer, this allows us to have custom colors
 * and customize other stuff without being dependend on vanilla code
 * 
 * @author immortaleeb
 *
 */
public class RenderTileEntityBeacon extends TileEntitySpecialRenderer {
	
	private static final ResourceLocation BEACON_TEXTURE = new ResourceLocation("textures/entity/beacon_beam.png");
	
	@Override
	public void render(TileEntity tileentity, double x, double y, double z, float partialTickTime, int partialDamage, float alpha) {
		renderBeacon((EvilCraftBeaconTileEntity)tileentity, x, y, z, partialTickTime, partialDamage);
	}
	
	protected void renderBeacon(EvilCraftBeaconTileEntity tileentity, double x, double y, double z, float partialTickTime, int partialDamage) {
		float f1 = tileentity.getBeamRenderVariable();
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
        GlStateManager.disableFog();
		
        if (tileentity.isBeamActive())
        {
        	Vector4f beamColor = tileentity.getBeamColor();
            this.bindTexture(BEACON_TEXTURE);
            TileEntityBeaconRenderer.renderBeamSegment(x, y, z, partialTickTime, f1,
                    tileentity.getWorld().getTotalWorldTime(), 0, 256,
                    new float[]{beamColor.x, beamColor.y, beamColor.z, beamColor.w});
        }

        GlStateManager.enableFog();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.5F);
	}

    @Override
    public boolean isGlobalRenderer(TileEntity te) {
        return true;
    }
}
