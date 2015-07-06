package evilcraft.client.render.entity;

import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.MobConfig;
import evilcraft.entity.monster.VengeanceSpirit;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Renderer for a vengeance spirit
 * 
 * @author rubensworks
 *
 */
public class RenderVengeanceSpirit extends Render {
    
    /**
     * Make a new instance.
     * @param config Then config.
     */
    public RenderVengeanceSpirit(RenderManager renderManager, ExtendedConfig<MobConfig> config) {
        super(renderManager);
    }

	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTickTime) {
		VengeanceSpirit spirit = (VengeanceSpirit) entity;
		EntityLivingBase innerEntity = spirit.getInnerEntity();
		if(innerEntity != null && spirit.isVisible()) {
			Render render = (Render) renderManager.entityRenderMap.get(innerEntity.getClass());
			if(render != null && !spirit.isSwarm()) {
				GL11.glEnable(GL11.GL_BLEND);
				if(!spirit.isFrozen()) {
					GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
				} else {
					GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
				}
                float c = Math.min(1F - (float) (spirit.getBuildupDuration()) / 30, 0.65F);
                GL11.glColor3f(c, c, c);
				//GL14.glBlendColor(0, 0, 0, 0);
				//GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_CONSTANT_COLOR);
				//GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE_MINUS_DST_COLOR);
				
				try {
					render.doRender(innerEntity, x, y, z, yaw, 0);
				} catch (Exception e) {
					// Invalid entity, so set as swarm.
					spirit.setIsSwarm(true);
				}
				GL11.glDisable(GL11.GL_BLEND);
			}
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity var1) {
		return null;
	}

}
