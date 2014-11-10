package evilcraft.client.render.item;

import evilcraft.client.render.model.ModelPedestal;
import evilcraft.core.client.render.item.RenderModelWavefrontItem;
import evilcraft.core.client.render.model.ModelWavefront;
import net.minecraft.util.ResourceLocation;


/**
 * Render the dark tank as item.
 * @author rubensworks
 *
 */
public class RenderItemSanguinaryPedestal extends RenderModelWavefrontItem {

    /**
     * Make a new instance.
     * @param model The model to render.
     * @param texture The texture to render the model with.
     */
    public RenderItemSanguinaryPedestal(ModelWavefront model, ResourceLocation texture) {
        super(model, texture);
    }
    
    @Override
    protected void preRenderModel(final ItemRenderType type, float x, float y, float z) {
        ModelPedestal.setRedHue(currentItemStack.getItemDamage() == 1);
    }

}
