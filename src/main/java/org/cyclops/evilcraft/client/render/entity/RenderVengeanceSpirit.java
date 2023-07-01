package org.cyclops.evilcraft.client.render.entity;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.properties.Property;
import com.mojang.blaze3d.vertex.PoseStack;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ArrowLayer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpirit;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpiritConfig;

import java.util.Map;

/**
 * Renderer for a vengeance spirit
 *
 * @author rubensworks
 *
 */
public class RenderVengeanceSpirit extends EntityRenderer<EntityVengeanceSpirit> {

    private final RenderPlayerSpirit playerRenderer;
    private final Map<GameProfile, GameProfile> checkedProfiles = Maps.newHashMap();

    public RenderVengeanceSpirit(EntityRendererProvider.Context context, EntityVengeanceSpiritConfig config) {
        super(context);
        playerRenderer = new RenderPlayerSpirit(context);
    }

    @Override
    public void render(EntityVengeanceSpirit spirit, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        super.render(spirit, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        Mob innerEntity = spirit.getInnerEntity();
        if(innerEntity != null && spirit.isVisible()) {
            EntityRenderer render = entityRenderDispatcher.renderers.get(innerEntity.getType());
            if(render != null && !spirit.isSwarm()) {
                // Override the render type buffer so that it always returns buffers with alpha blend
                MultiBufferSource bufferSub = renderType -> {
                    float uv = spirit.isFrozen() ? ((float)spirit.tickCount + partialTicks) * 0.01F : 1;
                    renderType = RenderType.energySwirl((spirit.isPlayer() ? playerRenderer : render).getTextureLocation(innerEntity), uv, uv);
                    return bufferIn.getBuffer(renderType);
                };

                try {
                    // Make new PoseStack, to fix stack invalidity when a crash occurs.
                    PoseStack poseStackInner = new PoseStack();
                    poseStackInner.last().pose().set(matrixStackIn.last().pose());
                    poseStackInner.last().normal().set(matrixStackIn.last().normal());

                    if(spirit.isPlayer()) {
                        GameProfile gameProfile = new GameProfile(spirit.getPlayerUUID(), spirit.getPlayerName());
                        ResourceLocation resourcelocation = DefaultPlayerSkin.getDefaultSkin();
                        Minecraft minecraft = Minecraft.getInstance();
                        // Check if we have loaded the (texturized) profile before, otherwise we load it and cache it.
                        if(!checkedProfiles.containsKey(gameProfile)) {
                            Property property = (Property) Iterables.getFirst(gameProfile.getProperties().get("textures"), (Object) null);
                            if (property == null) {
                                // The game profile enhanced with texture information.
                                GameProfile newGameProfile = Minecraft.getInstance().getMinecraftSessionService().fillProfileProperties(gameProfile, true);
                                checkedProfiles.put(gameProfile, newGameProfile);
                            }
                        } else {
                            Map map = minecraft.getSkinManager().getInsecureSkinInformation(checkedProfiles.get(gameProfile));
                            if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                                resourcelocation = minecraft.getSkinManager().registerTexture((MinecraftProfileTexture) map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
                            }
                        }
                        playerRenderer.setPlayerTexture(resourcelocation);
                        Minecraft.getInstance().options.hideGui = true; // Disables player name tag rendering, which causes a crash due to our posestack hack.
                        playerRenderer.render(innerEntity, entityYaw, partialTicks, poseStackInner, bufferSub, packedLightIn);
                        Minecraft.getInstance().options.hideGui = false;
                    } else {
                        render.render(innerEntity, entityYaw, 0, poseStackInner, bufferSub, packedLightIn);
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
    public ResourceLocation getTextureLocation(EntityVengeanceSpirit entity) {
        return null;
    }

    public static class RenderPlayerSpirit extends LivingEntityRenderer<Mob, PlayerModel<Mob>> {

        @Setter
        private ResourceLocation playerTexture;

        public RenderPlayerSpirit(EntityRendererProvider.Context context) {
            super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false), 0.5F);
            this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
            this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
            this.addLayer(new ArrowLayer<>(context, this));
            this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
        }

        @Override
        public ResourceLocation getTextureLocation(Mob entity) {
            return playerTexture;
        }

    }

}
