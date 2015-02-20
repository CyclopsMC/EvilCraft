package evilcraft.client.render.tileentity;

import evilcraft.client.render.model.ModelPedestal;
import evilcraft.core.client.render.model.ModelWavefront;
import evilcraft.core.client.render.tileentity.RenderTileEntityModelWavefront;
import evilcraft.tileentity.TileSanguinaryPedestal;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

/**
 * Renderer for the {@link evilcraft.block.EntangledChalice}.
 * @author rubensworks
 *
 */
public class RenderTileEntitySanguinaryPedestal extends RenderTileEntityModelWavefront {

	/**
     * Make a new instance.
     * @param model The model to render.
     * @param texture The texture to render the model with.
     */
	public RenderTileEntitySanguinaryPedestal(ModelWavefront model,
                                              ResourceLocation texture) {
		super(model, texture);
	}

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f) {
		if(tile instanceof TileSanguinaryPedestal) {
            ModelPedestal.setRedHue(tile.getBlockMetadata() == 1);
		}
        super.renderTileEntityAt(tile, x, y, z, f);
	}
    
}
