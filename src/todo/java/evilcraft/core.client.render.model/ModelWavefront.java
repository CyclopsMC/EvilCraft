package evilcraft.core.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.WavefrontObject;

/**
 * Render a {@link WavefrontObject}.
 * @author rubensworks
 *
 */
public class ModelWavefront extends ModelBase {

	private WavefrontObject model;
	private ResourceLocation texture;
	
	/**
	 * Make a new instance.
	 * @param model The model.
	 * @param texture The texture.
	 */
	public ModelWavefront(WavefrontObject model, ResourceLocation texture) {
		this.model = model;
		this.texture = texture;
	}
	
	/**
	 * Render the model with it's texture.
	 */
	public void renderAll() {
    	RenderHelpers.bindTexture(texture);
    	model.renderAll();
    }
	
}
