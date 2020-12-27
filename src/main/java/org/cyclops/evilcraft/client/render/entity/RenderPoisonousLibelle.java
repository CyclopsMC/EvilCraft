package org.cyclops.evilcraft.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
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

	public RenderPoisonousLibelle(EntityRendererManager renderManager, EntityPoisonousLibelleConfig config, ModelPoisonousLibelle model, float par2) {
	    super(renderManager, config, model, par2);
	}

    @Override
    protected void preRenderCallback(EntityPoisonousLibelle entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        super.preRenderCallback(entitylivingbaseIn, matrixStackIn, partialTickTime);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180F));
        matrixStackIn.scale(-0.5F, 0.5F, -0.5F);
        matrixStackIn.translate(0, 0.5F, 0);
    }

}
