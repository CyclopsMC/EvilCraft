package org.cyclops.evilcraft.core.client.render;

import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * Renderer for nothing, simple.
 * 
 * @author rubensworks
 *
 */
public class RenderNull extends EntityRenderer<Entity> {

    public RenderNull(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public boolean shouldRender(Entity livingEntityIn, ClippingHelper camera, double camX, double camY, double camZ) {
        return false;
    }

    @Override
    public ResourceLocation getTextureLocation(Entity entity) {
        return null;
    }

}
