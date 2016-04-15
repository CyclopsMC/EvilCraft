package org.cyclops.evilcraft.core.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
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
public abstract class RenderModelLiving<T extends EntityLiving, M extends ModelBase> extends RenderLiving<T> {

    private ResourceLocation texture;
    
    /**
     * Make a new instance.
     * @param renderManager The render manager
     * @param config The config.
     * @param model The model that must be rendered.
     * @param par2 No idea...
     */
    @SuppressWarnings("rawtypes")
    public RenderModelLiving(RenderManager renderManager, ExtendedConfig config, ModelBase model, float par2) {
        super(renderManager, model, par2);
        texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITIES + config.getNamedId() + ".png");
    }

    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return texture;
    }
    
}
