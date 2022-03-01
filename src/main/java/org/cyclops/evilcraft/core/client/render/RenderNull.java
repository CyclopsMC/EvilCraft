package org.cyclops.evilcraft.core.client.render;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

/**
 * Renderer for nothing, simple.
 *
 * @author rubensworks
 *
 */
public class RenderNull extends EntityRenderer<Entity> {

    public RenderNull(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public boolean shouldRender(Entity livingEntityIn, Frustum camera, double camX, double camY, double camZ) {
        return false;
    }

    @Override
    public ResourceLocation getTextureLocation(Entity entity) {
        return null;
    }

}
