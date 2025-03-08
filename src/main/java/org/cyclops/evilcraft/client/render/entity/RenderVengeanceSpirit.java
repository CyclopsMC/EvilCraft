package org.cyclops.evilcraft.client.render.entity;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidArmorModel;
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
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.PlayerSkin;
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
public class RenderVengeanceSpirit extends EntityRenderer<EntityVengeanceSpirit, RenderStateVengeanceSpirit> {

    private final RenderPlayerSpirit playerRenderer;
    private final Map<GameProfile, GameProfile> checkedProfiles = Maps.newHashMap();

    public RenderVengeanceSpirit(EntityRendererProvider.Context context, EntityVengeanceSpiritConfig config) {
        super(context);
        playerRenderer = new RenderPlayerSpirit(context);
    }

    @Override
    public void render(RenderStateVengeanceSpirit renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(renderState, poseStack, bufferSource, packedLight);

        Mob innerEntity = renderState.spirit.getInnerEntity();
        if(innerEntity != null && renderState.spirit.isVisible()) {
            LivingEntityRenderer render = (LivingEntityRenderer) entityRenderDispatcher.renderers.get(innerEntity.getType());
            if(render != null && !renderState.spirit.isSwarm()) {
                LivingEntityRenderState innerRenderState = renderState.spirit.isPlayer() ? getPlayerRenderState(renderState) : (LivingEntityRenderState) render.createRenderState();
                if (!renderState.spirit.isPlayer()) {
                    render.extractRenderState(innerEntity, innerRenderState, renderState.partialTick);
                }
                // Override the render type buffer so that it always returns buffers with alpha blend
                MultiBufferSource bufferSub = renderType -> {
                    float uv = renderState.spirit.isFrozen() ? ((float)renderState.spirit.tickCount + renderState.partialTick) * 0.01F : 1;
                    renderType = RenderType.energySwirl(renderState.spirit.isPlayer() ? playerRenderer.getTextureLocation((PlayerRenderState) innerRenderState) : render.getTextureLocation(innerRenderState), uv, uv);
                    return bufferSource.getBuffer(renderType);
                };

                try {
                    // Make new PoseStack, to fix stack invalidity when a crash occurs.
                    PoseStack poseStackInner = new PoseStack();
                    poseStackInner.last().pose().set(poseStack.last().pose());
                    poseStackInner.last().normal().set(poseStack.last().normal());

                    if(renderState.spirit.isPlayer()) {
                        PlayerRenderState playerRenderState = (PlayerRenderState) innerRenderState;
                        GameProfile gameProfile = new GameProfile(renderState.spirit.getPlayerUUID(), renderState.spirit.getPlayerName());
                        ResourceLocation resourcelocation = DefaultPlayerSkin.getDefaultTexture();
                        Minecraft minecraft = Minecraft.getInstance();
                        // Check if we have loaded the (texturized) profile before, otherwise we load it and cache it.
                        if(!checkedProfiles.containsKey(gameProfile)) {
                            Property property = (Property) Iterables.getFirst(gameProfile.getProperties().get("textures"), (Object) null);
                            if (property == null) {
                                // The game profile enhanced with texture information.
                                GameProfile newGameProfile = Minecraft.getInstance().getMinecraftSessionService().fetchProfile(gameProfile.getId(), true).profile();
                                checkedProfiles.put(gameProfile, newGameProfile);
                            }
                        } else {
                            PlayerSkin skin = minecraft.getSkinManager().getInsecureSkin(checkedProfiles.get(gameProfile));
                            resourcelocation = skin.texture();
                            playerRenderState.skin = skin;
                        }
                        playerRenderer.setPlayerTexture(resourcelocation);
                        Minecraft.getInstance().options.hideGui = true; // Disables player name tag rendering, which causes a crash due to our posestack hack.
                        playerRenderer.render(playerRenderState, poseStackInner, bufferSub, packedLight);
                        Minecraft.getInstance().options.hideGui = false;
                    } else {
                        render.render(innerRenderState, poseStackInner, bufferSub, packedLight);
                    }
                } catch (Exception e) {
                    // Invalid entity, so set as swarm.
                    renderState.spirit.setSwarm(true);
                    renderState.spirit.setPlayerId(""); // Just in case the crash was caused by a player spirit.
                }
            }
        }
    }

    private PlayerRenderState getPlayerRenderState(RenderStateVengeanceSpirit renderState) {
        PlayerRenderState playerRenderState = new PlayerRenderState();

        playerRenderState.x = renderState.x;
        playerRenderState.y = renderState.y;
        playerRenderState.z = renderState.z;
        playerRenderState.ageInTicks = renderState.ageInTicks;
        playerRenderState.boundingBoxWidth = renderState.boundingBoxWidth;
        playerRenderState.boundingBoxHeight = renderState.boundingBoxHeight;
        playerRenderState.eyeHeight = renderState.eyeHeight;
        playerRenderState.distanceToCameraSq = renderState.distanceToCameraSq;
        playerRenderState.isInvisible = renderState.isInvisible;
        playerRenderState.isDiscrete = renderState.isDiscrete;
        playerRenderState.displayFireAnimation = renderState.displayFireAnimation;
        playerRenderState.passengerOffset = renderState.passengerOffset;
        playerRenderState.nameTag = renderState.nameTag;
        playerRenderState.nameTagAttachment = renderState.nameTagAttachment;
        playerRenderState.leashState = renderState.leashState;
        playerRenderState.partialTick = renderState.partialTick;

        return playerRenderState;
    }

    @Override
    public RenderStateVengeanceSpirit createRenderState() {
        return new RenderStateVengeanceSpirit();
    }

    @Override
    public void extractRenderState(EntityVengeanceSpirit entity, RenderStateVengeanceSpirit renderState, float partialTick) {
        super.extractRenderState(entity, renderState, partialTick);

        renderState.spirit = entity;
    }

    public static class RenderPlayerSpirit extends LivingEntityRenderer<Mob, PlayerRenderState, PlayerModel> {

        private ResourceLocation playerTexture;

        public RenderPlayerSpirit(EntityRendererProvider.Context context) {
            super(context, new PlayerModel(context.bakeLayer(ModelLayers.PLAYER), false), 0.5F);
            this.addLayer(
                    new HumanoidArmorLayer<>(
                            this,
                            new HumanoidArmorModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
                            new HumanoidArmorModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)),
                            context.getEquipmentRenderer()
                    )
            );
            this.addLayer(new ItemInHandLayer<>(this));
            this.addLayer(new ArrowLayer<>(this, context));
            this.addLayer(new CustomHeadLayer<>(this, context.getModelSet()));
        }

        public void setPlayerTexture(ResourceLocation playerTexture) {
            this.playerTexture = playerTexture;
        }

        @Override
        public PlayerRenderState createRenderState() {
            return new PlayerRenderState();
        }

        @Override
        public ResourceLocation getTextureLocation(PlayerRenderState renderState) {
            return playerTexture;
        }
    }

}
