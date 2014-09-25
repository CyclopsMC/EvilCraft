package evilcraft.client.render.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import evilcraft.entity.item.EntityItemDefinedRotation;

/**
 * Renderer for a dark stick entity item.
 * @author rubensworks
 *
 */
public class RenderEntityItemDarkStick extends Render {

	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTickTime) {
		EntityItemDefinedRotation item = (EntityItemDefinedRotation) entity;
		
		//GL11.glPushMatrix();
        //GL11.glTranslatef((float)x, (float)y, (float)z);
        
        GL11.glPushMatrix();
        GL11.glTranslatef(1F, 0.875F, 0.8F);
        GL11.glRotatef(25F, 1, 0, 0);
        GL11.glRotatef(25F, 0, 1, 0);
        GL11.glRotatef(item.getRotationYawHead(), 0, 1, 0);
        GL11.glScalef(2F, 2F, 2F);
        
        RenderItem.renderInFrame = true;
        item.hoverStart = 0.0F;
        RenderManager.instance.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
        RenderItem.renderInFrame = false;

        GL11.glPopMatrix();
        
        //GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return null;
	}

}
