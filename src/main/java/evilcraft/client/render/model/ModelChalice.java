package evilcraft.client.render.model;

import java.util.Map;
import java.util.Random;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.WavefrontObject;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Maps;

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
	
	private static Map<String, Long> seeds = Maps.newHashMap();
	private static int chaliceColor = 0;
	private static int gemColor = 0;
	
	/**
	 * Make a new instance.
	 * @param texture The texture.
	 * @param gem The gem model.
	 */
	public ModelChalice(ResourceLocation texture, ModelGem gem) {
		super(model, texture);
		this.gem = gem;
	}
	
	/**
	 * Set the color seed of the chalice.
	 * @param id Unique id of a chalice group.
	 */
	public static void setColorSeed(String id) {
		if(seeds.containsKey(id)) {
			long seed = seeds.get(id);
			chaliceColor = (int) (seed & ((2 << 24) - 1));
			gemColor = (int) (seed >> 24);
		} else {
			long res = id.hashCode();
			Random rand = new Random(res);
			chaliceColor = rand.nextInt(2 << 24);
			gemColor = rand.nextInt(2 << 24);
			seeds.put(id, ((long)chaliceColor) | ((long)gemColor << 24));
		}
	}
	
	@Override
    public void renderAll() {
		float r, g, b;
		
		r = (float)(chaliceColor >> 16 & 255) / 255.0F;
        g = (float)(chaliceColor >> 8 & 255) / 255.0F;
        b = (float)(chaliceColor & 255) / 255.0F;
    	GL11.glColor3f(r, g, b);
		
    	GL11.glTranslatef(0.5F, 0.24F, 0.5F);
    	GL11.glScalef(0.30F, 0.22F, 0.30F);
    	GL11.glRotatef(180F, 1F, 0F, 0F);
    	super.renderAll();
    	
    	r = (float)(gemColor >> 16 & 255) / 255.0F;
        g = (float)(gemColor >> 8 & 255) / 255.0F;
        b = (float)(gemColor & 255) / 255.0F;
    	GL11.glColor3f(r, g, b);
    	
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
