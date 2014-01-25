package evilcraft.api.render;

import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public abstract class ModelRenderLiving<M extends ModelBase> extends RenderLiving {

    private ResourceLocation texture;
    
    public ModelRenderLiving(ExtendedConfig config, ModelBase model, float par2) {
        super(model, par2);
        texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_MODELS + config.NAMEDID + ".png");
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return texture;
    }
    
}
