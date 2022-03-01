package org.cyclops.evilcraft.client.render.entity;

import net.minecraft.client.model.AbstractZombieModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.entity.monster.EntityControlledZombie;
import org.cyclops.evilcraft.entity.monster.EntityControlledZombieConfig;

/**
 * Renderer for a controlled zombie.
 *
 * @author rubensworks
 *
 */
public class RenderControlledZombie extends HumanoidMobRenderer<EntityControlledZombie, AbstractZombieModel<EntityControlledZombie>> {

    private final ResourceLocation texture;

    public RenderControlledZombie(EntityControlledZombieConfig config, EntityRendererProvider.Context renderContext) {
        super(renderContext, new Model(renderContext), 0.5F);
        this.texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITIES + config.getNamedId() + ".png");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityControlledZombie entity) {
        return texture;
    }

    public static class Model extends AbstractZombieModel<EntityControlledZombie> {
        protected Model(EntityRendererProvider.Context context) {
            super(context.bakeLayer(ModelLayers.ZOMBIE));
        }

        @Override
        public boolean isAggressive(EntityControlledZombie p_212850_1_) {
            return p_212850_1_.isAggressive();
        }
    }

}
