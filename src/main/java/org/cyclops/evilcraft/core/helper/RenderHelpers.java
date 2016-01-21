package org.cyclops.evilcraft.core.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * A helper for rendering.
 * @author rubensworks
 *
 */
@SideOnly(Side.CLIENT)
public class RenderHelpers {
    
    /**
     * An icon that contains to texture, useful for when you want to render nothing.
     */
    public static TextureAtlasSprite EMPTYICON;

    /**
     * Texture sheet of all blocks.
     */
    public static ResourceLocation TEXTURE_MAP = TextureMap.locationBlocksTexture;
	
	/**
	 * Prepare a GL context for rendering fluids.
	 * @param fluid The fluid stack.
	 * @param x X
     * @param y Y
     * @param z Z
	 * @param render The actual fluid renderer.
	 */
	public static void renderFluidContext(FluidStack fluid, double x, double y, double z, IFluidContextRender render) {
		if(fluid != null && fluid.amount > 0) {
			GlStateManager.pushMatrix();

	        // Make sure both sides are rendered
	        GlStateManager.enableBlend();
	        GlStateManager.disableCull();
	        
	        // Correct color & lighting
	        GlStateManager.color(1, 1, 1, 1);
	        GlStateManager.disableLighting();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

	        // Set to current relative player location
			GlStateManager.translate(x, y, z);
	        
	        // Set blockState textures
	        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
	        
	        render.renderFluid(fluid);
	        
	        //GlStateManager.enableCull();
	        GlStateManager.enableLighting();
	        GlStateManager.disableBlend();
	        //GlStateManager.disableDepth();
	        GlStateManager.popMatrix();
		}
	}
	
	/**
	 * Prepare a GL context for rendering fluids for tile entities.
	 * @param fluid The fluid stack.
	 * @param x X
     * @param y Y
     * @param z Z
	 * @param tile The tile.
	 * @param render The actual fluid renderer.
	 */
	public static void renderTileFluidContext(final FluidStack fluid, final double x, final double y, final double z, final TileEntity tile, final IFluidContextRender render) {
		renderFluidContext(fluid, x, y, z, render);
	}
	
	/**
	 * Runnable for {@link RenderHelpers#renderFluidContext(FluidStack, double, double, double, IFluidContextRender)}.
	 * @author rubensworks
	 */
	public static interface IFluidContextRender {
		
		/**
		 * Render the fluid.
		 * @param fluid The fluid stack.
		 */
		public void renderFluid(FluidStack fluid);
		
	}
}
