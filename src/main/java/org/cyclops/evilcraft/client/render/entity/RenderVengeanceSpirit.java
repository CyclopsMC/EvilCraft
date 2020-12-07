package org.cyclops.evilcraft.client.render.entity;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.properties.Property;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.ArrowLayer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpirit;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpiritConfig;
import org.lwjgl.opengl.GL11;

import java.util.Map;
import java.util.Random;

/**
 * Renderer for a vengeance spirit
 * 
 * @author rubensworks
 *
 */
public class RenderVengeanceSpirit extends EntityRenderer<EntityVengeanceSpirit> {

	private final RenderPlayerSpirit playerRenderer;
	private final Map<GameProfile, GameProfile> checkedProfiles = Maps.newHashMap();

    public RenderVengeanceSpirit(EntityRendererManager renderManager, EntityVengeanceSpiritConfig config) {
        super(renderManager);
		playerRenderer = new RenderPlayerSpirit(renderManager);
    }

	@Override
	public void render(EntityVengeanceSpirit spirit, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
    	super.render(spirit, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
		MobEntity innerEntity = spirit.getInnerEntity();
		if(innerEntity != null && spirit.isVisible()) {
			EntityRenderer render = renderManager.renderers.get(innerEntity.getType());
			if(render != null && !spirit.isSwarm()) {
				// Override the render type buffer so that it always returns buffers with alpha blend
				IRenderTypeBuffer bufferSub = renderType -> {
					float uv = spirit.isFrozen() ? ((float)spirit.ticksExisted + partialTicks) * 0.01F : 1;
					renderType = RenderType.getEnergySwirl(render.getEntityTexture(innerEntity), uv, uv);
					return bufferIn.getBuffer(renderType);
				};
				
				try {
					if(spirit.isPlayer()) {
						GameProfile gameProfile = new GameProfile(spirit.getPlayerUUID(), spirit.getPlayerName());
						ResourceLocation resourcelocation = DefaultPlayerSkin.getDefaultSkinLegacy();
						Minecraft minecraft = Minecraft.getInstance();
						// Check if we have loaded the (texturized) profile before, otherwise we load it and cache it.
						if(!checkedProfiles.containsKey(gameProfile)) {
							Property property = (Property) Iterables.getFirst(gameProfile.getProperties().get("textures"), (Object) null);
							if (property == null) {
								// The game profile enchanced with texture information.
								GameProfile newGameProfile = Minecraft.getInstance().getSessionService().fillProfileProperties(gameProfile, true);
								checkedProfiles.put(gameProfile, newGameProfile);
							}
						} else {
							Map map = minecraft.getSkinManager().loadSkinFromCache(checkedProfiles.get(gameProfile));
							if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
								resourcelocation = minecraft.getSkinManager().loadSkin((MinecraftProfileTexture) map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
							}
						}
						playerRenderer.setPlayerTexture(resourcelocation);
						playerRenderer.render(innerEntity, entityYaw, partialTicks, matrixStackIn, bufferSub, packedLightIn);
					} else {
						render.render(innerEntity, entityYaw, 0, matrixStackIn, bufferSub, packedLightIn);
					}
				} catch (Exception e) {
					// Invalid entity, so set as swarm.
					spirit.setSwarm(true);
					spirit.setPlayerId(""); // Just in case the crash was caused by a player spirit.
				}
			}
		}
	}

	@Override
	public ResourceLocation getEntityTexture(EntityVengeanceSpirit entity) {
		return null;
	}

	public static class RenderPlayerSpirit extends BipedRenderer<MobEntity, PlayerModel<MobEntity>> {

		@Setter
		private ResourceLocation playerTexture;

		public RenderPlayerSpirit(EntityRendererManager renderManager) {
			super(renderManager, new PlayerModel<>(0.0F, false), 0.5F);
			PlayerModel modelPlayer = this.getEntityModel();
			this.addLayer(new BipedArmorLayer<>(this, new BipedModel(0.5F), new BipedModel(1.0F)));
			this.addLayer(new HeldItemLayer(this));
			this.addLayer(new ArrowLayer(this));
			this.addLayer(new HeadLayer<>(this));

			modelPlayer.setVisible(false);
			Random rand = new Random();
			modelPlayer.bipedHeadwear.showModel = rand.nextBoolean();
			modelPlayer.bipedBodyWear.showModel = rand.nextBoolean();
			modelPlayer.bipedLeftLegwear.showModel = rand.nextBoolean();
			modelPlayer.bipedRightLegwear.showModel = rand.nextBoolean();
			modelPlayer.bipedLeftArmwear.showModel = rand.nextBoolean();
			modelPlayer.bipedRightArmwear.showModel = rand.nextBoolean();
		}

		@Override
		public ResourceLocation getEntityTexture(MobEntity entity) {
			return playerTexture;
		}

	}

}
