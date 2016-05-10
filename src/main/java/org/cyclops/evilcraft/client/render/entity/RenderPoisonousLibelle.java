package org.cyclops.evilcraft.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.MobConfig;
import org.cyclops.evilcraft.core.client.render.entity.RenderModelLiving;
import org.cyclops.evilcraft.entity.monster.PoisonousLibelle;

/**
 * Renderer for a libelle
 * 
 * @author rubensworks
 *
 */
public class RenderPoisonousLibelle extends RenderModelLiving<PoisonousLibelle, ModelBase> {
    
    /**
     * Make a new instance
     * @param renderManager The render manager.
     * @param config The config.
     * @param model The model.
     * @param par2 No idea...
     */
	public RenderPoisonousLibelle(RenderManager renderManager, ExtendedConfig<MobConfig<PoisonousLibelle>> config, ModelBase model, float par2) {
	    super(renderManager, config, model, par2);
	}

    @Override
    protected void preRenderCallback(PoisonousLibelle entity, float f){
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        GlStateManager.translate(0, 0.5F, 0);
    }

	@Override
    public void doRender(PoisonousLibelle libelle, double x, double y, double z, float yaw, float partialTickTime) {
        super.doRender(libelle, x, y, z, yaw, partialTickTime);
    }

}
