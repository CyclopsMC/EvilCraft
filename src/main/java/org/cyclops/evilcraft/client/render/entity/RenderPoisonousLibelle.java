package org.cyclops.evilcraft.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.cyclops.evilcraft.core.client.render.entity.RenderModelLiving;
import org.cyclops.evilcraft.entity.monster.EntityPoisonousLibelle;
import org.cyclops.evilcraft.entity.monster.EntityPoisonousLibelleConfig;

/**
 * Renderer for a libelle
 *
 * @author rubensworks
 *
 */
public class RenderPoisonousLibelle extends RenderModelLiving<EntityPoisonousLibelle, RenderStatePoisonousLibelle, ModelPoisonousLibelle> {

    public RenderPoisonousLibelle(EntityRendererProvider.Context renderContext, EntityPoisonousLibelleConfig config, ModelPoisonousLibelle model, float par2) {
        super(renderContext, config, model, par2);
    }

    @Override
    protected void scale(RenderStatePoisonousLibelle renderState, PoseStack poseStack) {
        super.scale(renderState, poseStack);
        poseStack.mulPose(Axis.YP.rotationDegrees(180F));
        poseStack.scale(-0.5F, 0.5F, -0.5F);
        poseStack.translate(0, 0.5F, 0);
    }

    @Override
    public RenderStatePoisonousLibelle createRenderState() {
        return new RenderStatePoisonousLibelle();
    }

    @Override
    public void extractRenderState(EntityPoisonousLibelle entity, RenderStatePoisonousLibelle renderState, float partialTicks) {
        super.extractRenderState(entity, renderState, partialTicks);

        renderState.wingRotation = entity.getWingProgressScaled(0.2617994F);
    }
}
