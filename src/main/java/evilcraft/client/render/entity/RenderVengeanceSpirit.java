package evilcraft.client.render.entity;

import evilcraft.entity.monster.VengeanceSpirit;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.MobConfig;
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
				GlStateManager.enableBlend();
				if(!spirit.isFrozen()) {
					GlStateManager.blendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
				} else {
					GlStateManager.blendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
				}
                float c = Math.min(1F - (float) (spirit.getBuildupDuration()) / 30, 0.65F);
                GlStateManager.color(c, c, c);
				//GL14.glBlendColor(0, 0, 0, 0);
				//GlStateManager.blendFunc(GL11.GL_SRC_COLOR, GL11.GL_CONSTANT_COLOR);
				//GlStateManager.blendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE_MINUS_DST_COLOR);
				
				try {
					render.doRender(innerEntity, x, y, z, yaw, 0);
				} catch (Exception e) {
					// Invalid entity, so set as swarm.
					spirit.setIsSwarm(true);
				}
				GlStateManager.disableBlend();
			}
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity var1) {
		return null;
	}

}
