package org.cyclops.evilcraft.core.client.render;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * Renderer for nothing, simple.
 * 
 * @author rubensworks
 *
 */
public class RenderNull extends Render<Entity> {

    public RenderNull(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTickTime) { 	
    	
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }

}
