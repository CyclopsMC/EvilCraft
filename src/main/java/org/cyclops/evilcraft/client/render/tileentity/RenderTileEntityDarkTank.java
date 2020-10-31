package org.cyclops.evilcraft.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.helper.DirectionHelpers;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.evilcraft.block.BlockDarkTank;
import org.cyclops.evilcraft.tileentity.TileDarkTank;

/**
 * Renderer for the {@link BlockDarkTank}.
 * @author rubensworks
 *
 */
public class RenderTileEntityDarkTank extends TileEntityRenderer<TileDarkTank> {
	
	private static final double OFFSET = 0.01D;
	private static final double MINY = OFFSET;
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

	public RenderTileEntityDarkTank(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(final TileDarkTank tile, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		FluidStack fluid = tile.getTank().getFluid();
		RenderHelpers.renderFluidContext(fluid, matrixStackIn, () -> {
			float height = (float) (tile.getFillRatio() * 0.99F);
			int brightness = Math.max(combinedLightIn, fluid.getFluid().getAttributes().getLuminosity(fluid));
			renderFluidSides(height, tile.getTank().getFluid(), brightness, matrixStackIn, bufferIn);
		});
	}
	
	/**
	 * Render the fluid sides of the tank. (Not the tank itself!)
	 * @param height The fluid level.
	 * @param fluid The fluid.
     * @param brightness The brightness to render the fluid with.
	 */
	public static void renderFluidSides(float height, FluidStack fluid, int brightness, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn) {
        int l2 = brightness >> 0x10 & 0xFFFF;
        int i3 = brightness & 0xFFFF;
        Triple<Float, Float, Float> colorParts = RenderHelpers.getFluidVertexBufferColor(fluid);
        float r = colorParts.getLeft();
        float g = colorParts.getMiddle();
        float b = colorParts.getRight();
        float a = 1.0F;
		TextureAtlasSprite icon = RenderHelpers.getFluidIcon(fluid, Direction.UP);
		IVertexBuilder vb = bufferIn.getBuffer(RenderType.getText(icon.getAtlasTexture().getTextureLocation()));
		Matrix4f matrix = matrixStackIn.getLast().getMatrix();
		for(Direction side : DirectionHelpers.DIRECTIONS) {
			double[][] c = coordinates[side.ordinal()];
			float replacedMaxV = (side == Direction.UP || side == Direction.DOWN) ?
					icon.getMaxV() : ((icon.getMaxV() - icon.getMinV()) * height + icon.getMinV());
            vb.pos(c[0][0], getHeight(side, c[0][1], height), c[0][2]).color(r, g, b, a).tex(icon.getMinU(), replacedMaxV).lightmap(l2, i3).endVertex();
			vb.pos(c[1][0], getHeight(side, c[1][1], height), c[1][2]).color(r, g, b, a).tex(icon.getMinU(), icon.getMinV()).lightmap(l2, i3).endVertex();
			vb.pos(c[2][0], getHeight(side, c[2][1], height), c[2][2]).color(r, g, b, a).tex(icon.getMaxU(), icon.getMinV()).lightmap(l2, i3).endVertex();
			vb.pos(c[3][0], getHeight(side, c[3][1], height), c[3][2]).color(r, g, b, a).tex(icon.getMaxU(), replacedMaxV).lightmap(l2, i3).endVertex();
		}
	}
	
	private static double getHeight(Direction side, double height, double replaceHeight) {
		if(height == MAX) {
			return replaceHeight;
		}
		return height;
	}
    
}
