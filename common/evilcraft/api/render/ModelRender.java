package evilcraft.api.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig;

public abstract class ModelRender<M extends ModelBase> extends Render {
    
    private ResourceLocation texture;

    protected M model;
    
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
