package org.cyclops.evilcraft.core.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.evilcraft.Reference;

/**
 * A renderer for a custom model of an entity.
 * It will automatically take care of the texture.
 * @author rubensworks
 *
 * @param <T> The type of entity.
 * @param <M> The model that will be rendered.
 */
public abstract class RenderModelLiving<T extends MobEntity, M extends EntityModel<T>> extends MobRenderer<T, M> {

    private ResourceLocation texture;

    public RenderModelLiving(EntityRendererManager renderManager, ExtendedConfig<?, ?> config, M model, float par2) {
        super(renderManager, model, par2);
        texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITIES + config.getNamedId() + ".png");
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return texture;
    }
    
}
