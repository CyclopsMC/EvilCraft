package evilcraft.render.item;

import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import evilcraft.api.render.RenderItemModel;
import evilcraft.blocks.BoxOfEternalClosure;
import evilcraft.entities.tileentities.TileBoxOfEternalClosure;
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
    	float angle = TileBoxOfEternalClosure.START_LID_ANGLE;
    	if(BoxOfEternalClosure.getInstance().getSpiritId(currentItemStack) != null) {
    		angle = 0;
    	}
    	BoxOfEternalClosureModel box = ((BoxOfEternalClosureModel)model);
    	box.setCoverAngle(angle);
    	box.renderAll();
    }

}
