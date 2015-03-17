package evilcraft.client.render.entity;

import evilcraft.core.client.render.entity.RenderModelLiving;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.MobConfig;
import evilcraft.entity.monster.Werewolf;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;

/**
 * Renderer for a werewolf
 * 
 * @author rubensworks
 *
 */
public class RenderWerewolf extends RenderModelLiving<ModelBase> {
    
    /**
     * Make a new instance
     * @param config The config.
     * @param model The model.
     * @param par2 No idea...
     */
	public RenderWerewolf(RenderManager renderManager, ExtendedConfig<MobConfig> config, ModelBase model, float par2) {
	    super(renderManager, config, model, par2);
	}
	
	private void renderWerewolf(Werewolf werewolf, double x, double y, double z, float yaw, float partialTickTime) {
	    super.doRender(werewolf, x, y, z, yaw, partialTickTime);
    }

	@Override
    public void doRender(EntityLiving par1EntityLiving, double x, double y, double z, float yaw, float partialTickTime) {
        this.renderWerewolf((Werewolf)par1EntityLiving, x, y, z, yaw, partialTickTime);
    }

}
