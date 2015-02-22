package evilcraft.core.client.render.model;

import evilcraft.Reference;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * A renderer for a custom model.
 * It will automatically take care of the texture.
 * @author rubensworks
 *
 * @param <M> The model that will be rendered.
 */
public abstract class RenderModel<M extends ModelBase> extends Render {
    
    private ResourceLocation texture;

    protected M model;
    
    /**
     * Make a new instance.
     * @param config The config.
     */
    @SuppressWarnings("rawtypes")
    public RenderModel(ExtendedConfig config) {
        texture = createResourceLocation(config);
        model = constructModel();
    }

    protected ResourceLocation createResourceLocation(ExtendedConfig config) {
        return new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_MODELS + config.getNamedId() + ".png");
    }
    
    protected abstract M constructModel();

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return texture;
    }
    
}
