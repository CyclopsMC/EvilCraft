package evilcraft.api.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig;

/**
 * A renderer for a custom model.
 * It will automatically take care of the texture.
 * @author rubensworks
 *
 * @param <M> The model that will be rendered.
 */
public abstract class ModelRender<M extends ModelBase> extends Render {
    
    private ResourceLocation texture;

    protected M model;
    
    /**
     * Make a new instance.
     * @param config The config.
     */
    @SuppressWarnings("rawtypes")
    public ModelRender(ExtendedConfig config) {
        texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_MODELS + config.NAMEDID + ".png");
        model = constructModel();
    }
    
    protected abstract M constructModel();

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return texture;
    }
    
}
