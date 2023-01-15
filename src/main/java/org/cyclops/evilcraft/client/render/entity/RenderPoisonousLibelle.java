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
public class RenderPoisonousLibelle extends RenderModelLiving<EntityPoisonousLibelle, ModelPoisonousLibelle> {

    public RenderPoisonousLibelle(EntityRendererProvider.Context renderContext, EntityPoisonousLibelleConfig config, ModelPoisonousLibelle model, float par2) {
        super(renderContext, config, model, par2);
    }

    @Override
    protected void scale(EntityPoisonousLibelle entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(180F));
        matrixStackIn.scale(-0.5F, 0.5F, -0.5F);
        matrixStackIn.translate(0, 0.5F, 0);
    }

}
