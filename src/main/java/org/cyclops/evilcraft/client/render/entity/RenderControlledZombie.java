package org.cyclops.evilcraft.client.render.entity;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.MobConfig;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.entity.monster.ControlledZombie;

/**
 * Renderer for a netherfish
 * 
 * @author rubensworks
 *
 */
public class RenderControlledZombie extends RenderBiped<ControlledZombie> {

    private final ResourceLocation texture;

    /**
     * Make a new instance.
     * @param config The config
     * @param renderManager The rendermanager
     */
    public RenderControlledZombie(ExtendedConfig<MobConfig<ControlledZombie>> config, RenderManager renderManager) {
        super(renderManager, new ModelZombie(), 0.5F, 1.0F);
        this.texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITIES + config.getNamedId() + ".png");
    }
    
    @Override
    protected ResourceLocation getEntityTexture(ControlledZombie entity) {
        return texture;
    }

}
