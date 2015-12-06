package org.cyclops.evilcraft.client.render.tileentity;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.helper.DirectionHelpers;
import org.cyclops.evilcraft.block.DarkTank;
import org.cyclops.evilcraft.core.helper.RenderHelpers;
import org.cyclops.evilcraft.core.helper.RenderHelpers.IFluidContextRender;
import org.cyclops.evilcraft.tileentity.TileDarkTank;
import org.lwjgl.opengl.GL11;

/**
 * Renderer for the {@link DarkTank}.
 * @author rubensworks
 *
 */
public class RenderTileEntityDarkTank extends TileEntitySpecialRenderer{
	
	private static final double OFFSET = 0.01D;
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
            BlockPos pos = new BlockPos(x, y, z);
            Block block = tileEntity.getWorld().getBlockState(pos).getBlock();
            final int brightness = block.getMixedBrightnessForBlock(tileEntity.getWorld(), pos);
			RenderHelpers.renderTileFluidContext(fluid, x, y, z, tileEntity, new IFluidContextRender() {

				@Override
				public void renderFluid(FluidStack fluid) {
					double height = tank.getFillRatio() * 0.99D;
			        renderFluidSides(height, fluid, brightness);
				}
				
			});
		}
	}
	
	/**
	 * Render the fluid sides of the tank. (Not the tank itself!)
	 * @param height The fluid level.
	 * @param fluid The fluid.
	 */
	public static void renderFluidSides(double height, FluidStack fluid, int brightness) {
        int l2 = brightness >> 16 & 65535;
        int i3 = brightness & 65535;

		for(EnumFacing side : DirectionHelpers.DIRECTIONS) {
			TextureAtlasSprite icon = RenderHelpers.getFluidIcon(fluid, side);

            Tessellator t = Tessellator.getInstance();
            WorldRenderer worldRenderer = t.getWorldRenderer();
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
			
			double[][] c = coordinates[side.ordinal()];
			double replacedMaxV = (side == EnumFacing.UP || side == EnumFacing.DOWN) ?
					icon.getMaxV() : ((icon.getMaxV() - icon.getMinV()) * height + icon.getMinV());
            worldRenderer.pos(c[0][0], getHeight(side, c[0][1], height), c[0][2]).tex(icon.getMinU(), replacedMaxV).lightmap(l2, i3).endVertex();
            worldRenderer.pos(c[1][0], getHeight(side, c[1][1], height), c[1][2]).tex(icon.getMinU(), icon.getMinV()).lightmap(l2, i3).endVertex();
            worldRenderer.pos(c[2][0], getHeight(side, c[2][1], height), c[2][2]).tex(icon.getMaxU(), icon.getMinV()).lightmap(l2, i3).endVertex();
            worldRenderer.pos(c[3][0], getHeight(side, c[3][1], height), c[3][2]).tex(icon.getMaxU(), replacedMaxV).lightmap(l2, i3).endVertex();
			
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
