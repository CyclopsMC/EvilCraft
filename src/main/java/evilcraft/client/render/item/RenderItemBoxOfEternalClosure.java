package evilcraft.client.render.item;

import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import evilcraft.block.BoxOfEternalClosure;
import evilcraft.client.render.model.ModelBoxOfEternalClosure;
import evilcraft.core.client.render.item.RenderModelItem;
import evilcraft.tileentity.TileBoxOfEternalClosure;

/**
 * Item renderer for the {@link BoxOfEternalClosure}.
 * @author rubensworks
 *
 */
public class RenderItemBoxOfEternalClosure extends RenderModelItem {
    
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
    	if(BoxOfEternalClosure.getInstance().getSpiritName(currentItemStack) != null) {
    		angle = 0;
    	}
    	ModelBoxOfEternalClosure box = ((ModelBoxOfEternalClosure)model);
    	box.setCoverAngle(angle);
    	box.renderAll();
    }

}
