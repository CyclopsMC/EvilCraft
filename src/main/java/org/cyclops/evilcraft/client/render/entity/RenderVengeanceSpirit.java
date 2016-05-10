package org.cyclops.evilcraft.client.render.entity;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.properties.Property;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.MobConfig;
import org.cyclops.evilcraft.entity.monster.VengeanceSpirit;
import org.lwjgl.opengl.GL11;

import java.util.Map;
import java.util.Random;

/**
 * Renderer for a vengeance spirit
 * 
 * @author rubensworks
 *
 */
public class RenderVengeanceSpirit extends Render<VengeanceSpirit> {

	private final RenderPlayerSpirit playerRenderer;
	private final Map<GameProfile, GameProfile> checkedProfiles = Maps.newHashMap();

	/**
     * Make a new instance.
	 * @param renderManager The render manager.
     * @param config Then config.
     */
    public RenderVengeanceSpirit(RenderManager renderManager, ExtendedConfig<MobConfig<VengeanceSpirit>> config) {
        super(renderManager);
		playerRenderer = new RenderPlayerSpirit(renderManager);
    }

	@Override
	public void doRender(VengeanceSpirit spirit, double x, double y, double z, float yaw, float partialTickTime) {
		EntityLiving innerEntity = (EntityLiving) spirit.getInnerEntity();
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
					if(spirit.isPlayer()) {
						GameProfile gameProfile = new GameProfile(spirit.getPlayerUUID(), spirit.getPlayerName());
						ResourceLocation resourcelocation = DefaultPlayerSkin.getDefaultSkinLegacy();
						Minecraft minecraft = Minecraft.getMinecraft();
						// Check if we have loaded the (texturized) profile before, otherwise we load it and cache it.
						if(!checkedProfiles.containsKey(gameProfile)) {
							Property property = (Property) Iterables.getFirst(gameProfile.getProperties().get("textures"), (Object) null);
							if (property == null) {
								// The game profile enchanced with texture information.
								GameProfile newGameProfile = Minecraft.getMinecraft().getSessionService().fillProfileProperties(gameProfile, true);
								checkedProfiles.put(gameProfile, newGameProfile);
							}
						} else {
							Map map = minecraft.getSkinManager().loadSkinFromCache(checkedProfiles.get(gameProfile));
							if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
								resourcelocation = minecraft.getSkinManager().loadSkin((MinecraftProfileTexture) map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
							}
						}
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
				GlStateManager.disableBlend();
			}
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(VengeanceSpirit entity) {
		return null;
	}

	public static class RenderPlayerSpirit extends RenderBiped {

		@Setter
		private ResourceLocation playerTexture;

		public RenderPlayerSpirit(RenderManager renderManager) {
			super(renderManager, new ModelPlayer(0.0F, false), 0.5F);
			ModelPlayer modelPlayer = ((ModelPlayer) this.getMainModel());
			this.addLayer(new LayerBipedArmor(this));
			this.addLayer(new LayerHeldItem(this));
			this.addLayer(new LayerArrow(this));
			this.addLayer(new LayerCustomHead(modelPlayer.bipedHead));

			modelPlayer.setInvisible(true);
			Random rand = new Random();
			modelPlayer.bipedHeadwear.showModel = rand.nextBoolean();
			modelPlayer.bipedBodyWear.showModel = rand.nextBoolean();
			modelPlayer.bipedLeftLegwear.showModel = rand.nextBoolean();
			modelPlayer.bipedRightLegwear.showModel = rand.nextBoolean();
			modelPlayer.bipedLeftArmwear.showModel = rand.nextBoolean();
			modelPlayer.bipedRightArmwear.showModel = rand.nextBoolean();
		}

		protected ResourceLocation getEntityTexture(EntityLiving entity) {
			return playerTexture;
		}

	}

}
