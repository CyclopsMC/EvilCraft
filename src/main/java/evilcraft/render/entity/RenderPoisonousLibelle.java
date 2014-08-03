package evilcraft.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLiving;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.MobConfig;
import evilcraft.api.render.ModelRenderLiving;
import evilcraft.entities.monster.PoisonousLibelle;

/**
 * Renderer for a libelle
 * 
 * @author rubensworks
 *
 */
public class RenderPoisonousLibelle extends ModelRenderLiving<ModelBase> {
    
    /**
     * Make a new instance
     * @param config The config.
     * @param model The model.
     * @param par2 No idea...
     */
	public RenderPoisonousLibelle(ExtendedConfig<MobConfig> config, ModelBase model, float par2) {
	    super(config, model, par2);
	}
	
	private void renderLibelle(PoisonousLibelle libelle, double x, double y, double z, float yaw, float partialTickTime) {
	    super.doRender(libelle, x, y, z, yaw, partialTickTime);
    }

	@Override
    public void doRender(EntityLiving par1EntityLiving, double x, double y, double z, float yaw, float partialTickTime) {
        this.renderLibelle((PoisonousLibelle)par1EntityLiving, x, y, z, yaw, partialTickTime);
    }

}
