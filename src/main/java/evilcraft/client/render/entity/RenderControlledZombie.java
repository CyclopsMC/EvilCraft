package evilcraft.client.render.entity;

import evilcraft.Reference;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.MobConfig;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * Renderer for a netherfish
 * 
 * @author rubensworks
 *
 */
public class RenderControlledZombie extends RenderBiped {

    private final ResourceLocation texture;

    /**
     * Make a new instance.
     */
    public RenderControlledZombie(ExtendedConfig<MobConfig> config) {
        super(new ModelZombie(), 0.5F, 1.0F);
        this.texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITIES + config.getNamedId() + ".png");
    }
    
    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return texture;
    }

}
