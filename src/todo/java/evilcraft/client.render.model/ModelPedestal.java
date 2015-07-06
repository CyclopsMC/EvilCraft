package evilcraft.client.render.model;

import evilcraft.Reference;
import evilcraft.core.client.render.model.ModelWavefront;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.WavefrontObject;
import org.lwjgl.opengl.GL11;

/**
 * Model for a pedestal.
 * @author rubensworks
 *
 */
public class ModelPedestal extends ModelWavefront {
	
	private static WavefrontObject model = new WavefrontObject(
			new ResourceLocation(Reference.MOD_ID, Reference.MODEL_PATH + "pedestal.obj"));
    private static boolean redHue = false;
	
	/**
	 * Make a new instance.
	 * @param texture The texture.
	 */
	public ModelPedestal(ResourceLocation texture) {
		super(model, texture);
	}

    /**
     * Enable or disable red hue of the pedestal.
     * @param redHue If red
     */
    public static void setRedHue(boolean redHue) {
        ModelPedestal.redHue = redHue;
    }

	@Override
    public void renderAll() {
        if(redHue) {
            GL11.glColor3f(0.9F, 0.1F, 0.1F);
        }
    	GL11.glTranslatef(0.5F, 0.41F, 0.5F);
    	GL11.glScalef(0.30F, 0.26F, 0.30F);
    	GL11.glRotatef(180F, 1F, 0F, 0F);
    	super.renderAll();
    }

}
