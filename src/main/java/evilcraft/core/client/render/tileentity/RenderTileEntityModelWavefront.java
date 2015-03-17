package evilcraft.core.client.render.tileentity;

import evilcraft.core.client.render.model.ModelWavefront;
import evilcraft.core.tileentity.EvilCraftTileEntity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;

/**
 * General renderer for {@link EvilCraftTileEntity} with {@link ModelWavefront} models.
 * @author rubensworks
 *
 */
public class RenderTileEntityModelWavefront extends RenderTileEntityModel {
	
    /**
     * Make a new instance.
     * @param model The model to render.
     * @param texture The texture to render the model with.
     */
    public RenderTileEntityModelWavefront(ModelWavefront model, ResourceLocation texture) {
        super(model, texture);
    }
	
	/**
     * Render the actual model, override this to change the way the model should be rendered.
     */
    @Override
	protected void renderModel(EvilCraftTileEntity tile, ModelBase model, float partialTick) {
    	((ModelWavefront) model).renderAll();
    }
}
