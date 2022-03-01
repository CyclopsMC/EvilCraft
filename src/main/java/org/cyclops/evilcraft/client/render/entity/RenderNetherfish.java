package org.cyclops.evilcraft.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SilverfishRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Silverfish;
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

    public RenderNetherfish(EntityRendererProvider.Context renderContext, EntityNetherfishConfig config) {
        super(renderContext);
        texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITIES + config.getNamedId() + ".png");
    }

    @Override
    public ResourceLocation getTextureLocation(Silverfish entity) {
        return texture;
    }

}
