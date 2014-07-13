package evilcraft.render.tileentity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import evilcraft.api.entities.tileentitites.EvilCraftTileEntity;
import evilcraft.api.render.TileEntityModelRenderer;
import evilcraft.blocks.BoxOfEternalClosure;
import evilcraft.render.models.BoxOfEternalClosureModel;

/**
 * Renderer for the {@link BoxOfEternalClosure}.
 * @author rubensworks
 *
 */
public class TileEntityBoxOfEternalClosureRenderer extends TileEntityModelRenderer {

	/**
     * Make a new instance.
     * @param model The model to render.
     * @param texture The texture to render the model with.
     */
    public TileEntityBoxOfEternalClosureRenderer(ModelBase model, ResourceLocation texture) {
        super(model, texture);
    }

    @Override
    protected void renderModel(EvilCraftTileEntity tile, ModelBase model, float partialTick) {
    	((BoxOfEternalClosureModel)model).renderAll();
    }
}
