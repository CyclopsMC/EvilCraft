package org.cyclops.evilcraft.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SilverfishRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.entity.monster.EntityNetherfishConfig;

/**
 * Renderer for a netherfish
 *
 * @author rubensworks
 *
 */
public class RenderNetherfish extends SilverfishRenderer {

    private final Identifier texture;

    public RenderNetherfish(EntityRendererProvider.Context renderContext, EntityNetherfishConfig config) {
        super(renderContext);
        texture = Identifier.fromNamespaceAndPath(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITIES + config.getNamedId() + ".png");
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState renderState) {
        return texture;
    }
}
