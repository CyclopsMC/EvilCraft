package org.cyclops.evilcraft.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SilverfishRenderer;
import net.minecraft.entity.monster.SilverfishEntity;
import net.minecraft.util.ResourceLocation;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.entity.monster.EntityNetherfishConfig;

/**
 * Renderer for a netherfish
 * 
 * @author rubensworks
 *
 */
public class RenderNetherfish extends SilverfishRenderer {
    
    private final ResourceLocation texture;

    public RenderNetherfish(EntityRendererManager renderManager, EntityNetherfishConfig config) {
        super(renderManager);
        texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITIES + config.getNamedId() + ".png");
    }

    @Override
    public ResourceLocation getTextureLocation(SilverfishEntity entity) {
        return texture;
    }

}
