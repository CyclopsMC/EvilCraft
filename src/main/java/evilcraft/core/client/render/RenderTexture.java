package evilcraft.core.client.render;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Renderer for any texture
 * 
 * @author rubensworks
 *
 */
public class RenderTexture extends Render {
	
	private float scale;
	private ResourceLocation texture;
	
	/**
	 * Make a new instance.
	 * @param scale The scale.
	 * @param texture The texture
	 */
    public RenderTexture(float scale, ResourceLocation texture) {
        this.scale = scale;
        this.texture = texture;
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTickTime) { 	
    	GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        GL11.glScalef(this.scale / 1.0F, this.scale / 1.0F, this.scale / 1.0F);
        GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);

        this.bindTexture(texture);
        GL11.glScaled(scale, scale, scale);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(0, 0);
        GL11.glVertex3d(-1, -1, 0);
        GL11.glTexCoord2d(0, 1);
        GL11.glVertex3d(-1, 1, 0);
        GL11.glTexCoord2d(1, 1);
        GL11.glVertex3d(1, 1, 0);
        GL11.glTexCoord2d(1, 0);
        GL11.glVertex3d(1, -1, 0);
        GL11.glEnd();
        
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }

}
