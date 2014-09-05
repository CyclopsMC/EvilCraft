package evilcraft.render.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import evilcraft.core.config.ExtendedConfig;
import evilcraft.core.config.MobConfig;
import evilcraft.entities.monster.VengeanceSpirit;

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
    public RenderVengeanceSpirit(ExtendedConfig<MobConfig> config) {
        
    }

	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTickTime) {
		VengeanceSpirit spirit = (VengeanceSpirit) entity;
		EntityLivingBase innerEntity = spirit.getInnerEntity();
		if(innerEntity != null && spirit.isVisible()) {
			Render render = (Render) RenderManager.instance.entityRenderMap.get(innerEntity.getClass());
			if(render != null && !spirit.isSwarm()) {
				GL11.glEnable(GL11.GL_BLEND);
				if(!spirit.isFrozen()) {
					GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
				} else {
					GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
				}
				//GL14.glBlendColor(0, 0, 0, 0);
				//GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_CONSTANT_COLOR);
				//GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE_MINUS_DST_COLOR);
				
				render.doRender(innerEntity, x, y, z, yaw, 0);
				GL11.glDisable(GL11.GL_BLEND);
			}
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity var1) {
		return null;
	}

}
