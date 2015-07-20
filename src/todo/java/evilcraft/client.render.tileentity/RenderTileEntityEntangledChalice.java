package evilcraft.client.render.tileentity;

import org.cyclops.evilcraft.block.EntangledChalice;
import evilcraft.client.render.model.ModelChalice;
import evilcraft.core.client.render.model.ModelWavefront;
import evilcraft.core.client.render.tileentity.RenderTileEntityModelWavefront;
import org.cyclops.evilcraft.core.fluid.WorldSharedTank;
import org.cyclops.evilcraft.core.helper.RenderHelpers;
import org.cyclops.evilcraft.core.helper.RenderHelpers.IFluidContextRender;
import org.cyclops.evilcraft.tileentity.TileEntangledChalice;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

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
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f, int partialDamage) {
		if(tile instanceof TileEntangledChalice) {
			final TileEntangledChalice chalice = ((TileEntangledChalice) tile);
			ModelChalice.setColorSeed(((WorldSharedTank) chalice.getTank()).getTankID());
            super.renderTileEntityAt(tile, x, y, z, f, partialDamage);
	
			FluidStack fluid = chalice.getTank().getFluid();
			RenderHelpers.renderTileFluidContext(fluid, new BlockPos(x, y, z), tile, new IFluidContextRender() {

				@Override
				public void renderFluid(FluidStack fluid) {
					renderFluidSide(fluid, chalice.getFillRatio());
				}
				
			});
		}
	}
	
	/**
	 * Render the fluid contents.
	 * @param fluid The fluid.
	 * @param fillRatio The fill ratio of the tank.
	 */
	public static void renderFluidSide(FluidStack fluid, double fillRatio) {
		double height = fillRatio * 0.17D + 0.765F;
		float vertexOffset = Math.max(0.5F, Math.min(0.1F, (MAX - MIN) / 2 * ((float) (1.0D - fillRatio) * 0.9F + 0.005F)));
		float min = MIN + vertexOffset;
		float max = MAX - vertexOffset;
		float iconScale = (float) (1.0D - fillRatio) / (4F);
		
		TextureAtlasSprite icon = RenderHelpers.getFluidIcon(fluid, EnumFacing.UP);
			
		Tessellator t = Tessellator.getInstance();
        WorldRenderer worldRenderer = t.getWorldRenderer();
        worldRenderer.startDrawingQuads();
		
		double minU = (icon.getMaxU() - icon.getMinU()) * iconScale + icon.getMinU();
		double maxU = icon.getMaxU() - (icon.getMaxU() - icon.getMinU()) * iconScale;
		double minV = (icon.getMaxV() - icon.getMinV()) * iconScale + icon.getMinV();
		double maxV = icon.getMaxV() - (icon.getMaxV() - icon.getMinV()) * iconScale;

        worldRenderer.addVertexWithUV(min, height, min, minU, maxV);
        worldRenderer.addVertexWithUV(min, height, max, minU, minV);
        worldRenderer.addVertexWithUV(max, height, max, maxU, minV);
        worldRenderer.addVertexWithUV(max, height, min, maxU, maxV);
			
		t.draw();
	}
    
}
