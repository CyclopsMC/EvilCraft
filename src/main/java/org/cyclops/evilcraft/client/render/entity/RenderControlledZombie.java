package org.cyclops.evilcraft.client.render.entity;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.zombie.AbstractZombieModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.resources.Identifier;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.entity.monster.EntityControlledZombie;
import org.cyclops.evilcraft.entity.monster.EntityControlledZombieConfig;

/**
 * Renderer for a controlled zombie.
 *
 * @author rubensworks
 *
 */
public class RenderControlledZombie extends HumanoidMobRenderer<EntityControlledZombie, ZombieRenderState, AbstractZombieModel<ZombieRenderState>> {

    private final Identifier texture;

    public RenderControlledZombie(EntityControlledZombieConfig config, EntityRendererProvider.Context renderContext) {
        super(renderContext, new Model(renderContext), 0.5F);
        this.texture = Identifier.fromNamespaceAndPath(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITIES + config.getNamedId() + ".png");
    }

    @Override
    public Identifier getTextureLocation(ZombieRenderState renderState) {
        return texture;
    }

    @Override
    public ZombieRenderState createRenderState() {
        return new ZombieRenderState();
    }

    @Override
    public void extractRenderState(EntityControlledZombie entity, ZombieRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.isAggressive = entity.isAggressive();
        state.isConverting = false;
    }

    public static class Model extends AbstractZombieModel<ZombieRenderState> {
        protected Model(EntityRendererProvider.Context context) {
            super(context.bakeLayer(ModelLayers.ZOMBIE));
        }
    }

}
