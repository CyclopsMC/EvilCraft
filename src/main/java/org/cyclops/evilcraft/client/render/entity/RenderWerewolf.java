package org.cyclops.evilcraft.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.cyclops.evilcraft.core.client.render.entity.RenderModelLiving;
import org.cyclops.evilcraft.entity.monster.EntityWerewolf;
import org.cyclops.evilcraft.entity.monster.EntityWerewolfConfig;

/**
 * Renderer for a werewolf
 *
 * @author rubensworks
 *
 */
public class RenderWerewolf extends RenderModelLiving<EntityWerewolf, RenderStateWerewolf, ModelWerewolf> {

    public RenderWerewolf(EntityRendererProvider.Context renderContext, EntityWerewolfConfig config, ModelWerewolf model, float par2) {
        super(renderContext, config, model, par2);
    }

    @Override
    public RenderStateWerewolf createRenderState() {
        return new RenderStateWerewolf();
    }

    @Override
    public void extractRenderState(EntityWerewolf entity, RenderStateWerewolf renderState, float partialTicks) {
        super.extractRenderState(entity, renderState, partialTicks);

        renderState.barkProgress = entity.getBarkProgressScaled(0.1745329F);
    }
}
