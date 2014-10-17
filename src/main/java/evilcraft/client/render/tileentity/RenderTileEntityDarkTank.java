package evilcraft.client.render.tileentity;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import evilcraft.block.DarkTank;
import evilcraft.core.helper.DirectionHelpers;
import evilcraft.core.helper.RenderHelpers;
import evilcraft.core.helper.RenderHelpers.IFluidContextRender;
import evilcraft.tileentity.TileDarkTank;

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
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
		if(tileEntity instanceof TileDarkTank) {
			final TileDarkTank tank = ((TileDarkTank) tileEntity);
			FluidStack fluid = tank.getTank().getFluid();
			RenderHelpers.renderTileFluidContext(fluid, x, y, z, tileEntity, new IFluidContextRender() {

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
		for(ForgeDirection side : DirectionHelpers.DIRECTIONS) {
			IIcon icon = RenderHelpers.getFluidIcon(fluid, side);
			
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
