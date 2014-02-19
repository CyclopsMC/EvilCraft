package evilcraft.api.render;

import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * A renderer for a custom model of an entity.
 * It will automatically take care of the texture.
 * @author rubensworks
 *
 * @param <M> The model that will be rendered.
 */
public abstract class ModelRenderLiving<M extends ModelBase> extends RenderLiving {

    private ResourceLocation texture;
    
    /**
     * Make a new instance.
     * @param config The config.
     * @param model The model that must be rendered.
     * @param par2 No idea...
     */
    @SuppressWarnings("rawtypes")
    public ModelRenderLiving(ExtendedConfig config, ModelBase model, float par2) {
        super(model, par2);
        texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITIES + config.NAMEDID + ".png");
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return texture;
    }
    
}
