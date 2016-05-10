package org.cyclops.evilcraft.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.MobConfig;
import org.cyclops.evilcraft.core.client.render.entity.RenderModelLiving;
import org.cyclops.evilcraft.entity.monster.Werewolf;

/**
 * Renderer for a werewolf
 * 
 * @author rubensworks
 *
 */
public class RenderWerewolf extends RenderModelLiving<Werewolf, ModelBase> {
    
    /**
     * Make a new instance
	 * @param renderManager The render manager.
     * @param config The config.
     * @param model The model.
     * @param par2 No idea...
     */
	public RenderWerewolf(RenderManager renderManager, ExtendedConfig<MobConfig<Werewolf>> config, ModelBase model, float par2) {
	    super(renderManager, config, model, par2);
	}
	
	private void renderWerewolf(Werewolf werewolf, double x, double y, double z, float yaw, float partialTickTime) {
	    super.doRender(werewolf, x, y, z, yaw, partialTickTime);
    }

	@Override
    public void doRender(Werewolf werewolf, double x, double y, double z, float yaw, float partialTickTime) {
        this.renderWerewolf(werewolf, x, y, z, yaw, partialTickTime);
    }

}
