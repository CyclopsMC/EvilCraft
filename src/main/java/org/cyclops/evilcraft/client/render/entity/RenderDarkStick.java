package org.cyclops.evilcraft.client.render.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.evilcraft.entity.item.EntityItemDarkStick;
import org.lwjgl.opengl.GL11;

/**
 * Renderer for a dark stick
 * 
 * @author rubensworks
 *
 */
public class RenderDarkStick extends Render<EntityItemDarkStick> {

    /**
     * Make a new instance.
     * @param renderManager The render manager
     * @param config The config.
     */
	public RenderDarkStick(RenderManager renderManager, ExtendedConfig<EntityConfig<EntityItemDarkStick>> config) {
	    super(renderManager);
	}

    protected ItemStack getItemStack(EntityItemDarkStick entity) {
        return entity.getEntityItem();
    }

	@Override
	public void doRender(EntityItemDarkStick entity, double x, double y, double z, float yaw, float partialTickTime) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y + 0.2F, z);

        float rotation;
        if(entity.isValid()) {
            rotation = entity.getAngle();
        } else {
            rotation = (((float)entity.getAge()) / 20.0F + entity.hoverStart) * (180F / (float)Math.PI);
        }

        GL11.glRotatef(rotation, 0, 1, 0);
        GL11.glRotatef(-90F, 0, 1, 0);
        GL11.glRotatef(25F, 1, 0, 0);
        
        bindEntityTexture(entity);

        RenderHelpers.renderItem(getItemStack(entity));
        
        GlStateManager.popMatrix();
	}

    @Override
    protected ResourceLocation getEntityTexture(EntityItemDarkStick entity) {
        return null;
    }

}
