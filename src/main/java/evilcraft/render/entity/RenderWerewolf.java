package evilcraft.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.MobConfig;
import evilcraft.api.render.ModelRenderLiving;
import evilcraft.entities.monster.Werewolf;

/**
 * Renderer for a werewolf
 * 
 * @author rubensworks
 *
 */
public class RenderWerewolf extends ModelRenderLiving<ModelBase> {
    
    /**
     * Make a new instance
     * @param config The config.
     * @param model The model.
     * @param par2 No idea...
     */
	public RenderWerewolf(ExtendedConfig<MobConfig> config, ModelBase model, float par2) {
	    super(config, model, par2);
	}
	
	private void renderWerewolf(Werewolf werewolf, double x, double y, double z, float yaw, float partialTickTime) {
	    super.doRender(werewolf, x, y, z, yaw, partialTickTime);
    }

	@Override
    public void doRender(EntityLiving par1EntityLiving, double x, double y, double z, float yaw, float partialTickTime) {
        this.renderWerewolf((Werewolf)par1EntityLiving, x, y, z, yaw, partialTickTime);
    }

	@Override
	public void doRender(Entity par1Entity, double x, double y, double z, float yaw, float partialTickTime) {
        this.renderWerewolf((Werewolf)par1Entity, x, y, z, yaw, partialTickTime);
    }

}
