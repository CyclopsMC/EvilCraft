package evilcraft.core.helper;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
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

import java.util.HashMap;
import java.util.Map;

/**
 * A helper for rendering.
 * @author rubensworks
 *
 */
public class RenderHelpers {
    
    private static Map<EnumFacing, String> METHODS_RENDERFACE = new HashMap<EnumFacing, String>();
    static {
        METHODS_RENDERFACE.put(EnumFacing.DOWN, "renderFaceYNeg");
        METHODS_RENDERFACE.put(EnumFacing.UP, "renderFaceYPos");
        METHODS_RENDERFACE.put(EnumFacing.NORTH, "renderFaceZPos");
        METHODS_RENDERFACE.put(EnumFacing.EAST, "renderFaceXPos");
        METHODS_RENDERFACE.put(EnumFacing.SOUTH, "renderFaceZNeg");
        METHODS_RENDERFACE.put(EnumFacing.WEST, "renderFaceXNeg");
    }
    private static Map<EnumFacing, String> FIELDS_UVROTATE = new HashMap<EnumFacing, String>();
    static { // Note: the fields from the RenderBlock are INCORRECT! Very good read: http://greyminecraftcoder.blogspot.be/2013/07/rendering-non-standard-blocks.html
        FIELDS_UVROTATE.put(EnumFacing.DOWN, "uvRotateBottom");
        FIELDS_UVROTATE.put(EnumFacing.UP, "uvRotateTop");
        FIELDS_UVROTATE.put(EnumFacing.NORTH, "uvRotateEast");
        FIELDS_UVROTATE.put(EnumFacing.EAST, "uvRotateSouth");
        FIELDS_UVROTATE.put(EnumFacing.SOUTH, "uvRotateWest");
        FIELDS_UVROTATE.put(EnumFacing.WEST, "uvRotateNorth");
    }
    private static Map<EnumFacing, String> METHODS_RENDERFACE_OBFUSICATED = new HashMap<EnumFacing, String>();
    static {
        METHODS_RENDERFACE_OBFUSICATED.put(EnumFacing.DOWN, "func_147768_a");
        METHODS_RENDERFACE_OBFUSICATED.put(EnumFacing.UP, "func_147806_b");
        METHODS_RENDERFACE_OBFUSICATED.put(EnumFacing.NORTH, "func_147734_d");
        METHODS_RENDERFACE_OBFUSICATED.put(EnumFacing.EAST, "func_147764_f");
        METHODS_RENDERFACE_OBFUSICATED.put(EnumFacing.SOUTH, "func_147761_c");
        METHODS_RENDERFACE_OBFUSICATED.put(EnumFacing.WEST, "func_147798_e");
    }
    private static Map<EnumFacing, String> FIELDS_UVROTATE_OBFUSICATED = new HashMap<EnumFacing, String>();
    static { // Note: the fields from the RenderBlock are INCORRECT! Very good read: http://greyminecraftcoder.blogspot.be/2013/07/rendering-non-standard-blocks.html
        FIELDS_UVROTATE_OBFUSICATED.put(EnumFacing.DOWN, "field_147865_v");
        FIELDS_UVROTATE_OBFUSICATED.put(EnumFacing.UP, "field_147867_u");
        FIELDS_UVROTATE_OBFUSICATED.put(EnumFacing.NORTH, "field_147875_q");
        FIELDS_UVROTATE_OBFUSICATED.put(EnumFacing.EAST, "field_147871_s");
        FIELDS_UVROTATE_OBFUSICATED.put(EnumFacing.SOUTH, "field_147873_r");
        FIELDS_UVROTATE_OBFUSICATED.put(EnumFacing.WEST, "field_147869_t");
    }
    private static int[] ROTATE_UV_ROTATE = {0, 1, 3, 2}; // N, E, S, W -> N, E, W, S
    
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
        return Minecraft.getMinecraft().getBlockRendererDispatcher().func_175023_a().func_178122_a(block.getDefaultState());
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
	 * @param blockPos The position.
	 * @param render The actual fluid renderer.
	 */
	public static void renderFluidContext(FluidStack fluid, BlockPos blockPos, IFluidContextRender render) {
		if(fluid != null && fluid.amount > 0) {
			GL11.glPushMatrix();

	        // Make sure both sides are rendered
	        GL11.glDepthMask(true);
	        GL11.glDisable(GL11.GL_CULL_FACE);
	        
	        // Correct color & lighting
	        GL11.glColor4f(1, 1, 1, 1);
	        GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

	        // Set to current relative player location
	        GL11.glTranslated(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	        
	        // Set blockState textures
	        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
	        
	        render.renderFluid(fluid);
	        
	        //GL11.glEnable(GL11.GL_CULL_FACE);
	        GL11.glEnable(GL11.GL_LIGHTING);
	        GL11.glDisable(GL11.GL_BLEND);
	        //GL11.glDepthMask(false);
	        GL11.glPopMatrix();
		}
	}

    /**
     * Set the brightness of the world renderer.
     * @param brightness The brightness.
     */
    public static void setBrightness(int brightness) {
        Tessellator.getInstance().getWorldRenderer().func_178962_a(brightness, brightness, brightness, brightness);
    }
	
	/**
	 * Prepare a GL context for rendering fluids for tile entities.
	 * @param fluid The fluid stack.
	 * @param blockPos The position.
	 * @param tile The tile.
	 * @param render The actual fluid renderer.
	 */
	public static void renderTileFluidContext(final FluidStack fluid, final BlockPos blockPos, final TileEntity tile, final IFluidContextRender render) {
		renderFluidContext(fluid, blockPos, new IFluidContextRender() {
			
			@Override
			public void renderFluid(FluidStack fluid) {		        
		        // Make sure our lighting is correct, otherwise everything will be black -_-
		        Block block = tile.getWorld().getBlockState(blockPos).getBlock();
		        setBrightness(2 * block.getMixedBrightnessForBlock(tile.getWorld(), blockPos));
		        
		        // Call the actual render.
		        render.renderFluid(fluid);
			}
		});
	}
	
	/**
	 * Runnable for {@link RenderHelpers#renderFluidContext(FluidStack, BlockPos, IFluidContextRender)}.
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
