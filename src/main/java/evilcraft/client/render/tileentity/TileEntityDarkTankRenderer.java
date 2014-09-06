package evilcraft.client.render.tileentity;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import evilcraft.block.DarkTank;
import evilcraft.tileentity.TileDarkTank;

/**
 * Renderer for the {@link DarkTank}.
 * @author rubensworks
 *
 */
public class TileEntityDarkTankRenderer extends TileEntitySpecialRenderer{

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {

		TileDarkTank tank = ((TileDarkTank) tileEntity);

		FluidStack fluid = tank.getTank().getFluid();
		if(fluid != null && fluid.amount > 0) {
			// TODO: render the same way as FluidConverters, but use flowing icon at the sides.
		}
	}
    
}
