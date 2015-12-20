package org.cyclops.evilcraft.client.render.entity;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.MobConfig;
import org.cyclops.evilcraft.Reference;

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
     * @param config The config
     * @param renderManager The rendermanager
     */
    public RenderControlledZombie(ExtendedConfig<MobConfig> config, RenderManager renderManager) {
        super(renderManager, new ModelZombie(), 0.5F, 1.0F);
        this.texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITIES + config.getNamedId() + ".png");
    }
    
    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return texture;
    }

}
