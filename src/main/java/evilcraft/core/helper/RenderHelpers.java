package evilcraft.core.helper;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

/**
 * A helper for rendering.
 * @author rubensworks
 *
 */
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
     * Bind a texture to the rendering engine.
     * @param texture The texture to bind.
     */
    public static void bindTexture(ResourceLocation texture) {
    	Minecraft.getMinecraft().renderEngine.bindTexture(texture);
    }

	/**
	 * Convert r, g and b colors to an integer representation.
	 * @param r red
	 * @param g green
	 * @param b blue
	 * @return integer representation of the color.
	 */
	public static int RGBToInt(int r, int g, int b) {
	    return (int)r << 16 | (int)g << 8 | (int)b;
	}

    /**
     * Get the default icon from a block.
     * @param block The block.
     * @return The icon.
     */
    public static TextureAtlasSprite getBlockIcon(Block block) {
        return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(block.getDefaultState());
    }
	
	/**
	 * Get the icon of a fluid for a side in a safe way.
	 * @param fluid The fluid stack.
	 * @param side The side to get the icon from, UP if null.
	 * @return The icon.
	 */
	public static TextureAtlasSprite getFluidIcon(FluidStack fluid, EnumFacing side) {
		Block defaultBlock = Blocks.water;
		Block block = defaultBlock;
		if(fluid.getFluid().getBlock() != null) {
			block = fluid.getFluid().getBlock();
		}
		
		if(side == null) side = EnumFacing.UP;

        TextureAtlasSprite icon = fluid.getFluid().getFlowingIcon();
		if(icon == null || (side == EnumFacing.UP || side == EnumFacing.DOWN)) {
			icon = fluid.getFluid().getStillIcon();
		}
		if(icon == null) {
			icon = getBlockIcon(block);
			if(icon == null) {
				icon = getBlockIcon(defaultBlock);
			}
		}
		
		return icon;
	}
	
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
            // TODO: to glstatemanager
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
     * Set the brightness of the world renderer.
     * @param brightness The brightness.
     */
    public static void setBrightness(int brightness) {
        Tessellator.getInstance().getWorldRenderer().setBrightness(brightness);
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
		renderFluidContext(fluid, x, y, z, new IFluidContextRender() {
			
			@Override
			public void renderFluid(FluidStack fluid) {		        
		        // Make sure our lighting is correct, otherwise everything will be black -_-
                BlockPos pos = new BlockPos(x, y, z);
		        Block block = tile.getWorld().getBlockState(pos).getBlock();
		        setBrightness(2 * block.getMixedBrightnessForBlock(tile.getWorld(), pos));
		        
		        // Call the actual render.
		        render.renderFluid(fluid);
			}
		});
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
