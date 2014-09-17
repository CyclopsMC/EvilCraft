package evilcraft.core.client.render.item;

import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import evilcraft.core.client.render.model.ModelWavefront;

/**
 * General item renderer for blocks/items with models.
 * @author rubensworks
 *
 */
public class RenderModelWavefrontItem extends RenderModelItem {

	/**
     * Make a new instance.
     * @param model The model to render.
     * @param texture The texture to render the model with.
     */
    public RenderModelWavefrontItem(ModelWavefront model, ResourceLocation texture) {
        super(model, texture);
    }
    
    @Override
    protected void renderModel(ModelBase model) {
    	((ModelWavefront) model).renderAll();
    }
    
}
