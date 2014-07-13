package evilcraft.render.item;

import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import evilcraft.api.render.RenderItemModel;
import evilcraft.blocks.BoxOfEternalClosure;
import evilcraft.render.models.BoxOfEternalClosureModel;

/**
 * Item renderer for the {@link BoxOfEternalClosure}.
 * @author rubensworks
 *
 */
public class RenderItemBoxOfEternalClosure extends RenderItemModel {
    
	/**
     * Make a new instance.
     * @param model The model to render.
     * @param texture The texture to render the model with.
     */
    public RenderItemBoxOfEternalClosure(ModelBase model, ResourceLocation texture) {
        super(model, texture);
    }
    
    @Override
    protected void renderModel(ModelBase model) {
    	((BoxOfEternalClosureModel)model).renderAll();
    }

}
