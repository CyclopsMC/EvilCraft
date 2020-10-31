package org.cyclops.evilcraft.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
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
        GlStateManager.scalef(-0.5F, 0.5F, -0.5F);
        GlStateManager.translatef(0, 0.5F, 0);
    }

}
