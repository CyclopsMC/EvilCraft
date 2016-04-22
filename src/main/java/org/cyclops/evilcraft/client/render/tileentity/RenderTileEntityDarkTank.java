package org.cyclops.evilcraft.client.render.tileentity;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.helper.DirectionHelpers;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.evilcraft.block.DarkTank;
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
	public void renderTileEntityAt(final TileEntity tileEntity, final double x, final double y, final double z, float f, int partialDamage) {
		if(tileEntity instanceof TileDarkTank) {
			final TileDarkTank tank = ((TileDarkTank) tileEntity);
			FluidStack fluid = tank.getTank().getFluid();
            RenderHelpers.renderTileFluidContext(fluid, x, y, z, tileEntity, new RenderHelpers.IFluidContextRender() {

                @Override
                public void renderFluid(FluidStack fluid) {
                    double height = tank.getFillRatio() * 0.99D;
                    renderFluidSides(height, fluid, tileEntity.getWorld().getCombinedLight(tileEntity.getPos(), fluid.getFluid().getLuminosity(fluid)));
                }

            });
		}
	}
	
	/**
	 * Render the fluid sides of the tank. (Not the tank itself!)
	 * @param height The fluid level.
	 * @param fluid The fluid.
     * @param brightness The brightness to render the fluid with.
	 */
	public static void renderFluidSides(double height, FluidStack fluid, int brightness) {
        int l2 = brightness >> 0x10 & 0xFFFF;
        int i3 = brightness & 0xFFFF;
        Triple<Float, Float, Float> colorParts = RenderHelpers.getFluidVertexBufferColor(fluid);
        float r = colorParts.getLeft();
        float g = colorParts.getMiddle();
        float b = colorParts.getRight();
        float a = 1.0F;
		for(EnumFacing side : DirectionHelpers.DIRECTIONS) {
			TextureAtlasSprite icon = org.cyclops.cyclopscore.helper.RenderHelpers.getFluidIcon(fluid, side);

            Tessellator t = Tessellator.getInstance();
            VertexBuffer worldRenderer = t.getBuffer();
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
			
			double[][] c = coordinates[side.ordinal()];
			double replacedMaxV = (side == EnumFacing.UP || side == EnumFacing.DOWN) ?
					icon.getMaxV() : ((icon.getMaxV() - icon.getMinV()) * height + icon.getMinV());
            worldRenderer.pos(c[0][0], getHeight(side, c[0][1], height), c[0][2]).tex(icon.getMinU(), replacedMaxV).lightmap(l2, i3).color(r, g, b, a).endVertex();
            worldRenderer.pos(c[1][0], getHeight(side, c[1][1], height), c[1][2]).tex(icon.getMinU(), icon.getMinV()).lightmap(l2, i3).color(r, g, b, a).endVertex();
            worldRenderer.pos(c[2][0], getHeight(side, c[2][1], height), c[2][2]).tex(icon.getMaxU(), icon.getMinV()).lightmap(l2, i3).color(r, g, b, a).endVertex();
            worldRenderer.pos(c[3][0], getHeight(side, c[3][1], height), c[3][2]).tex(icon.getMaxU(), replacedMaxV).lightmap(l2, i3).color(r, g, b, a).endVertex();
			
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
