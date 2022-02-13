package org.cyclops.evilcraft.client.render.entity;

import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.AbstractZombieModel;
import net.minecraft.util.ResourceLocation;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.entity.monster.EntityControlledZombie;
import org.cyclops.evilcraft.entity.monster.EntityControlledZombieConfig;

/**
 * Renderer for a controlled zombie.
 * 
 * @author rubensworks
 *
 */
public class RenderControlledZombie extends BipedRenderer<EntityControlledZombie, AbstractZombieModel<EntityControlledZombie>> {

    private final ResourceLocation texture;

    public RenderControlledZombie(EntityControlledZombieConfig config, EntityRendererManager renderManager) {
        super(renderManager, new Model(0.0F, false), 0.5F);
        this.texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITIES + config.getNamedId() + ".png");
    }
    
    @Override
    public ResourceLocation getTextureLocation(EntityControlledZombie entity) {
        return texture;
    }

    public static class Model extends AbstractZombieModel<EntityControlledZombie> {
        protected Model(float modelSize, boolean p_i1168_2_) {
            super(modelSize, 0.0F, 64, p_i1168_2_ ? 32 : 64);
        }

        @Override
        public boolean isAggressive(EntityControlledZombie p_212850_1_) {
            return p_212850_1_.isAggressive();
        }
    }

}
