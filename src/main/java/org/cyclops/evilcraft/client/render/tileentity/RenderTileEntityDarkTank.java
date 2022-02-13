package org.cyclops.evilcraft.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.vector.Matrix4f;
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
import org.cyclops.evilcraft.client.render.model.ModelDarkTankBaked;
import org.cyclops.evilcraft.tileentity.TileDarkTank;

/**
 * Renderer for the {@link BlockDarkTank}.
 * @author rubensworks
 */
public class RenderTileEntityDarkTank extends TileEntityRenderer<TileDarkTank> {
	
	private static final float OFFSET = 0.01F;
	private static final float MINY = OFFSET;
	private static final float MIN = 0.125F + OFFSET;
	private static final float MAX = 0.875F - OFFSET;
	private static float[][][] coordinates = {
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
					{MIN, MAX, MAX},
					{MIN, MINY, MAX},
					{MAX, MINY, MAX},
					{MAX, MAX, MAX}

			},
			{ // WEST
					{MIN, MAX, MIN},
					{MIN, MINY, MIN},
					{MIN, MINY, MAX},
					{MIN, MAX, MAX}

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
			renderFluidSides(height, tile.getTank().getFluid(), tile.isEnabled(), brightness, matrixStackIn, bufferIn);
		});
	}

	public static void renderFluidSides(float height, FluidStack fluid, boolean flowing, int brightness, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn) {
        int l2 = brightness >> 0x10 & 0xFFFF;
        int i3 = brightness & 0xFFFF;
        Triple<Float, Float, Float> colorParts = RenderHelpers.getFluidVertexBufferColor(fluid);
        float r = colorParts.getLeft();
        float g = colorParts.getMiddle();
        float b = colorParts.getRight();
        float a = 1.0F;
		Matrix4f matrix = matrixStackIn.last().pose();
		for (Direction side : DirectionHelpers.DIRECTIONS) {
			TextureAtlasSprite icon = ModelDarkTankBaked.getFluidIcon(fluid, flowing, side);
			IVertexBuilder vb = bufferIn.getBuffer(RenderType.text(icon.atlas().location()));
			float[][] c = coordinates[side.ordinal()];
			float minV = icon.getV0();
			float maxV = (icon.getV1() - icon.getV0()) * height + icon.getV0();
			float minU = icon.getU0();
			float maxU = icon.getU1();
			if (side == Direction.WEST || side == Direction.SOUTH) {
				// Flip up-side down
				float tmp = minV;
				minV = maxV;
				maxV = tmp;
			} else if (side == Direction.UP || side == Direction.DOWN) {
				maxV = icon.getV1();
			}
            vb.vertex(matrix, c[0][0], getHeight(side, c[0][1], height), c[0][2]).color(r, g, b, a).uv(minU, maxV).uv2(l2, i3).endVertex();
			vb.vertex(matrix, c[1][0], getHeight(side, c[1][1], height), c[1][2]).color(r, g, b, a).uv(minU, minV).uv2(l2, i3).endVertex();
			vb.vertex(matrix, c[2][0], getHeight(side, c[2][1], height), c[2][2]).color(r, g, b, a).uv(maxU, minV).uv2(l2, i3).endVertex();
			vb.vertex(matrix, c[3][0], getHeight(side, c[3][1], height), c[3][2]).color(r, g, b, a).uv(maxU, maxV).uv2(l2, i3).endVertex();
		}
	}
	
	private static float getHeight(Direction side, float height, float replaceHeight) {
		if(height == MAX) {
			return replaceHeight;
		}
		return height;
	}
    
}
