package evilcraft.client.render.tileentity;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import evilcraft.block.DarkTank;
import evilcraft.core.helper.DirectionHelpers;
import evilcraft.tileentity.TileDarkTank;

/**
 * Renderer for the {@link DarkTank}.
 * @author rubensworks
 *
 */
public class TileEntityDarkTankRenderer extends TileEntitySpecialRenderer{
	
	private static final double OFFSET = 0.001D;
	private static final double MINY = 0.001D;
	private static final double MIN = 0.125D + OFFSET;
	private static final double MAX = 0.875D - OFFSET;
	private static double[][][] coordinates = {
        { // DOWN
            {MIN, MINY, MIN},
            {MIN, MINY, MAX},
            {MAX, MINY, MAX},
            {MAX, MINY, MIN}
        },
        { // UP
            {MIN, MAX, MIN},
            {MIN, MAX, MAX},
            {MAX, MAX, MAX},
            {MAX, MAX, MIN}
        },
        { // NORTH
            {MIN, MINY, MIN},
            {MIN, MAX, MIN},
            {MAX, MAX, MIN},
            {MAX, MINY, MIN}
        },
        { // SOUTH
            {MIN, MINY, MAX},
            {MIN, MAX, MAX},
            {MAX, MAX, MAX},
            {MAX, MINY, MAX}
        },
        { // WEST
            {MIN, MINY, MIN},
            {MIN, MAX, MIN},
            {MIN, MAX, MAX},
            {MIN, MINY, MAX}
        },
        { // EAST
            {MAX, MINY, MIN},
            {MAX, MAX, MIN},
            {MAX, MAX, MAX},
            {MAX, MINY, MAX}
        }
    };

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {

		TileDarkTank tank = ((TileDarkTank) tileEntity);

		FluidStack fluid = tank.getTank().getFluid();
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
	        GL11.glTranslated(x, y, z);

	        // Set block textures
	        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
	        
	        // Make sure our lighting is correct, otherwise everything will be black -_-
	        Block block = tileEntity.getWorldObj().getBlock((int) x, (int) y, (int) z);
	        Tessellator.instance.setBrightness(2 * block.getMixedBrightnessForBlock(tileEntity.getWorldObj(), (int) x, (int) y, (int) z));
	        
	        double height = tank.getFillRatio() / 1.01D;
	        renderFluidSides(height, fluid);
	        
	        //GL11.glEnable(GL11.GL_CULL_FACE);
	        GL11.glEnable(GL11.GL_LIGHTING);
	        GL11.glDisable(GL11.GL_BLEND);
	        //GL11.glDepthMask(false);
	        GL11.glPopMatrix();
		}
	}
	
	/**
	 * Render the fluid sides of the tank. (Not the tank itself!)
	 * @param height The fluid level.
	 * @param fluid The fluid.
	 */
	public static void renderFluidSides(double height, FluidStack fluid) {
		Block defaultBlock = Blocks.water;
		Block block = defaultBlock;
		if(fluid.getFluid().getBlock() != null) {
			block = fluid.getFluid().getBlock();
		}
		
		for(ForgeDirection side : DirectionHelpers.DIRECTIONS) {
			IIcon icon = block.getIcon(side.ordinal(), 0);
			if(icon == null) {
				icon = defaultBlock.getIcon(side.ordinal(), 0);
			}
			
			Tessellator t = Tessellator.instance;
			t.startDrawingQuads();
			
			double[][] c = coordinates[side.ordinal()];
			double replacedMaxV = (side == ForgeDirection.UP || side == ForgeDirection.DOWN) ?
					icon.getMaxV() : ((icon.getMaxV() - icon.getMinV()) * height + icon.getMinV());
			t.addVertexWithUV(c[0][0], getHeight(side, c[0][1], height), c[0][2], icon.getMinU(), replacedMaxV);
			t.addVertexWithUV(c[1][0], getHeight(side, c[1][1], height), c[1][2], icon.getMinU(), icon.getMinV());
			t.addVertexWithUV(c[2][0], getHeight(side, c[2][1], height), c[2][2], icon.getMaxU(), icon.getMinV());
			t.addVertexWithUV(c[3][0], getHeight(side, c[3][1], height), c[3][2], icon.getMaxU(), replacedMaxV);
			
			t.draw();
		}
	}
	
	private static double getHeight(ForgeDirection side, double height, double replaceHeight) {
		if(height == MAX) {
			return replaceHeight;
		}
		return height;
	}
    
}
