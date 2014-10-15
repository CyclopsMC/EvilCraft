package evilcraft.client.render.tileentity;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import evilcraft.block.EntangledChalice;
import evilcraft.core.client.render.model.ModelWavefront;
import evilcraft.core.client.render.tileentity.RenderTileEntityModelWavefront;
import evilcraft.core.helper.RenderHelpers;
import evilcraft.core.helper.RenderHelpers.IFluidContextRender;
import evilcraft.tileentity.TileEntangledChalice;

/**
 * Renderer for the {@link EntangledChalice}.
 * @author rubensworks
 *
 */
public class RenderTileEntityEntangledChalice extends RenderTileEntityModelWavefront {

	private static final float MIN = 0.20F;
	private static final float MAX = 0.80F;
	
	/**
     * Make a new instance.
     * @param model The model to render.
     * @param texture The texture to render the model with.
     */
	public RenderTileEntityEntangledChalice(ModelWavefront model,
			ResourceLocation texture) {
		super(model, texture);
	}

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f) {
		super.renderTileEntityAt(tile, x, y, z, f);
		if(tile instanceof TileEntangledChalice) {
			final TileEntangledChalice tank = ((TileEntangledChalice) tile);
	
			FluidStack fluid = tank.getTank().getFluid();
			RenderHelpers.renderFluidContext(fluid, x, y, z, tile, new IFluidContextRender() {

				@Override
				public void renderFluid(TileEntity tile, FluidStack fluid) {
					double height = tank.getFillRatio() * 0.19D + 0.765F;
					float vertexOffset = (MAX - MIN) / 2 * ((float) (1.0D - tank.getFillRatio()) * 0.9F + 0.1F);
					renderFluidSide(height, fluid, MIN + vertexOffset, MAX - vertexOffset, (float) (1.0D - tank.getFillRatio()) / 2);
				}
				
			});
		}
	}
	
	/**
	 * Render the fluid contents.
	 * @param height The fluid level.
	 * @param fluid The fluid.
	 * @param min The minimum X and Z vertex.
	 * @param max The maximum X and Z vertex.
	 * @param iconScale The scale for the UV of the icon.
	 */
	public static void renderFluidSide(double height, FluidStack fluid, float min, float max, float iconScale) {		
		IIcon icon = RenderHelpers.getFluidIcon(fluid, ForgeDirection.UP);
			
		Tessellator t = Tessellator.instance;
		t.startDrawingQuads();
		
		double minU = (icon.getMaxU() - icon.getMinU()) * iconScale + icon.getMinU();
		double maxU = icon.getMaxU() - (icon.getMaxU() - icon.getMinU()) * iconScale;
		double minV = (icon.getMaxV() - icon.getMinV()) * iconScale + icon.getMinV();
		double maxV = icon.getMaxV() - (icon.getMaxV() - icon.getMinV()) * iconScale;
		
		t.addVertexWithUV(min, height, min, minU, maxV);
		t.addVertexWithUV(min, height, max, minU, minV);
		t.addVertexWithUV(max, height, max, maxU, minV);
		t.addVertexWithUV(max, height, min, maxU, maxV);
			
		t.draw();
	}
    
}
