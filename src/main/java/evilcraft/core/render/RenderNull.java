package evilcraft.core.render;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * Renderer for nothing, simple.
 * 
 * @author rubensworks
 *
 */
public class RenderNull extends Render {

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTickTime) { 	
    	
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }

}
