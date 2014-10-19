package evilcraft.client.render.model;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.WavefrontObject;

import org.lwjgl.opengl.GL11;

import evilcraft.Reference;
import evilcraft.core.client.render.model.ModelWavefront;

/**
 * Model for a chalice.
 * @author rubensworks
 *
 */
public class ModelChalice extends ModelWavefront {
	
	private static WavefrontObject model = new WavefrontObject(
			new ResourceLocation(Reference.MOD_ID, Reference.MODEL_PATH + "chalice.obj"));
	
	private ModelGem gem;
	
	/**
	 * Make a new instance.
	 * @param texture The texture.
	 * @param gem The gem model.
	 */
	public ModelChalice(ResourceLocation texture, ModelGem gem) {
		super(model, texture);
		this.gem = gem;
	}
	
	@Override
    public void renderAll() {
    	GL11.glTranslatef(0.5F, 0.24F, 0.5F);
    	GL11.glScalef(0.30F, 0.22F, 0.30F);
    	GL11.glRotatef(180F, 1F, 0F, 0F);
    	super.renderAll();
    	
    	GL11.glColor3f(1.0F, 0.0F, 0.4F); // TODO: depend on seed
    	GL11.glScalef(0.4F, 0.4F, 0.4F);
    	
    	for(int i = 0; i < 4; i++) {
    		GL11.glPushMatrix();
    		GL11.glRotatef(i * 90F, 0F, 1F, 0F);
	    	GL11.glTranslatef(-1.8F, 1.6F, -0.5F);
	    	GL11.glRotatef(195F, 0F, 0F, 1F);
	    	for(int j = -1; j <= 1; j+=2) {
	    		GL11.glPushMatrix();
	    		GL11.glTranslatef(0F, 0F, j * 1.2F);
	    		gem.renderAll();
	    		GL11.glPopMatrix();
	    	}
	    	GL11.glPopMatrix();
    	}
    	
    }

}
