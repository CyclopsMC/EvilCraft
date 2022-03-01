package org.cyclops.evilcraft.core.client.render.entity;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
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
public abstract class RenderModelLiving<T extends Mob, M extends EntityModel<T>> extends MobRenderer<T, M> {

    private ResourceLocation texture;

    public RenderModelLiving(EntityRendererProvider.Context context, ExtendedConfig<?, ?> config, M model, float par2) {
        super(context, model, par2);
        texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITIES + config.getNamedId() + ".png");
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return texture;
    }

}
