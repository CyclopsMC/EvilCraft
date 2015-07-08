package evilcraft.client.render.tileentity;

import evilcraft.block.DarkTank;
import org.cyclops.cyclopscore.helper.DirectionHelpers;
import evilcraft.core.helper.RenderHelpers;
import evilcraft.core.helper.RenderHelpers.IFluidContextRender;
import evilcraft.tileentity.TileDarkTank;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;

/**
 * Renderer for the {@link DarkTank}.
 * @author rubensworks
 *
 */
public class RenderTileEntityDarkTank extends TileEntitySpecialRenderer{
	
	private static final double OFFSET = 0.001D;
	private static final double MINY = 0.002D;
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
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f, int partialDamage) {
		if(tileEntity instanceof TileDarkTank) {
			final TileDarkTank tank = ((TileDarkTank) tileEntity);
			FluidStack fluid = tank.getTank().getFluid();
			RenderHelpers.renderTileFluidContext(fluid, new BlockPos(x, y, z), tileEntity, new IFluidContextRender() {

				@Override
				public void renderFluid(FluidStack fluid) {
					double height = tank.getFillRatio() * 0.99D;
			        renderFluidSides(height, fluid);
				}
				
			});
		}
	}
	
	/**
	 * Render the fluid sides of the tank. (Not the tank itself!)
	 * @param height The fluid level.
	 * @param fluid The fluid.
	 */
	public static void renderFluidSides(double height, FluidStack fluid) {
		for(EnumFacing side : DirectionHelpers.DIRECTIONS) {
			TextureAtlasSprite icon = RenderHelpers.getFluidIcon(fluid, side);
			
			Tessellator t = Tessellator.getInstance();
            WorldRenderer worldRenderer = t.getWorldRenderer();
            worldRenderer.startDrawingQuads();
			
			double[][] c = coordinates[side.ordinal()];
			double replacedMaxV = (side == EnumFacing.UP || side == EnumFacing.DOWN) ?
					icon.getMaxV() : ((icon.getMaxV() - icon.getMinV()) * height + icon.getMinV());
            worldRenderer.addVertexWithUV(c[0][0], getHeight(side, c[0][1], height), c[0][2], icon.getMinU(), replacedMaxV);
            worldRenderer.addVertexWithUV(c[1][0], getHeight(side, c[1][1], height), c[1][2], icon.getMinU(), icon.getMinV());
            worldRenderer.addVertexWithUV(c[2][0], getHeight(side, c[2][1], height), c[2][2], icon.getMaxU(), icon.getMinV());
            worldRenderer.addVertexWithUV(c[3][0], getHeight(side, c[3][1], height), c[3][2], icon.getMaxU(), replacedMaxV);
			
			t.draw();
		}
	}
	
	private static double getHeight(EnumFacing side, double height, double replaceHeight) {
		if(height == MAX) {
			return replaceHeight;
		}
		return height;
	}
    
}
