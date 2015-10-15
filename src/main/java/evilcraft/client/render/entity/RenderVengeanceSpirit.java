package evilcraft.client.render.entity;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.MobConfig;
import evilcraft.entity.monster.VengeanceSpirit;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.Map;
import java.util.UUID;

/**
 * Renderer for a vengeance spirit
 * 
 * @author rubensworks
 *
 */
public class RenderVengeanceSpirit extends Render {

	private final RenderPlayerSpirit playerRenderer = new RenderPlayerSpirit();
    
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
                float c = Math.min(1F - (float) (spirit.getBuildupDuration()) / 30, 0.65F);
                GL11.glColor3f(c, c, c);
				//GL14.glBlendColor(0, 0, 0, 0);
				//GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_CONSTANT_COLOR);
				//GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE_MINUS_DST_COLOR);
				
				try {
					if(spirit.isPlayer()) {
						GameProfile gameProfile = new GameProfile(null, spirit.getPlayerName());
						ResourceLocation resourcelocation = AbstractClientPlayer.locationStevePng;
						Minecraft minecraft = Minecraft.getMinecraft();
						Map map = minecraft.func_152342_ad().func_152788_a(gameProfile);
						if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
							resourcelocation = minecraft.func_152342_ad().func_152792_a((MinecraftProfileTexture)map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
						}
						playerRenderer.setRenderManager(this.renderManager);
						playerRenderer.setPlayerTexture(resourcelocation);
						playerRenderer.doRender(innerEntity, x, y, z, yaw, 0);
					} else {
						render.doRender(innerEntity, x, y, z, yaw, 0);
					}
				} catch (Exception e) {
					// Invalid entity, so set as swarm.
					spirit.setIsSwarm(true);
					spirit.setPlayerId(""); // Just in case the crash was caused by a player spirit.
				}
				GL11.glDisable(GL11.GL_BLEND);
			}
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity var1) {
		return null;
	}

	public static class RenderPlayerSpirit extends RenderBiped {

		@Setter
		private ResourceLocation playerTexture;

		public RenderPlayerSpirit() {
			super(new ModelBiped(0.0F), 0.5F);
		}

		protected ResourceLocation getEntityTexture(EntityLiving entity) {
			return playerTexture;
		}

	}

}
