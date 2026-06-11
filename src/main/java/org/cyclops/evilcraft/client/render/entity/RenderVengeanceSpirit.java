package org.cyclops.evilcraft.client.render.entity;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ArrowLayer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpirit;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpiritConfig;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

import java.util.List;
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
    public void submit(RenderStateVengeanceSpirit renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        super.submit(renderState, poseStack, nodeCollector, cameraRenderState);

        Mob innerEntity = renderState.spirit.getInnerEntity();
        if(innerEntity != null && renderState.spirit.isVisible()) {
            EntityRenderer render = entityRenderDispatcher.renderers.get(innerEntity.getType());
            if(render != null && !renderState.spirit.isSwarm()) {
                try {
                    EntityRenderState innerRenderState = renderState.spirit.isPlayer() ? getAvatarRenderState(renderState) : render.createRenderState();
                    if (!renderState.spirit.isPlayer()) {
                        render.extractRenderState(innerEntity, innerRenderState, 0);
                    }
                    float uv = renderState.spirit.isFrozen() ? ((float)renderState.spirit.tickCount + renderState.partialTick) * 0.01F : 1;
                    // Make new PoseStack, to fix stack invalidity when a crash occurs.
                    PoseStack poseStackInner = new PoseStack();
                    poseStackInner.last().pose().set(poseStack.last().pose());
                    poseStackInner.last().normal().set(poseStack.last().normal());

                    if(renderState.spirit.isPlayer()) {
                        AvatarRenderState avatarRenderState = (AvatarRenderState) innerRenderState;
                        GameProfile gameProfile = new GameProfile(renderState.spirit.getPlayerUUID(), renderState.spirit.getPlayerName());
                        Identifier resourcelocation = DefaultPlayerSkin.getDefaultTexture();
                        Minecraft minecraft = Minecraft.getInstance();
                        // Check if we have loaded the (texturized) profile before, otherwise we load it and cache it.
                        if(!checkedProfiles.containsKey(gameProfile)) {
                            Property property = (Property) Iterables.getFirst(gameProfile.properties().get("textures"), (Object) null);
                            if (property == null) {
                                // The game profile enhanced with texture information.
                                Minecraft.getInstance().services().profileResolver().fetchById(gameProfile.id()).ifPresent(newGameProfile -> checkedProfiles.put(gameProfile, newGameProfile));
                            }
                        } else {
                            PlayerSkin skin = minecraft.getSkinManager().createLookup(checkedProfiles.get(gameProfile), false).get();
                            resourcelocation = skin.body().texturePath();
                            avatarRenderState.skin = skin;

                        }
                        playerRenderer.setPlayerTexture(resourcelocation);
                        Minecraft.getInstance().options.hideGui = true; // Disables player name tag rendering, which causes a crash due to our posestack hack.
                        RenderType renderTypeOverride = RenderTypes.energySwirl(playerRenderer.getTextureLocation((AvatarRenderState) innerRenderState), uv, uv);
                        playerRenderer.submit(avatarRenderState, poseStackInner, new SubmitNodeCollectorRenderTypeOverride(nodeCollector, renderTypeOverride), cameraRenderState);
                        Minecraft.getInstance().options.hideGui = false;
                    } else {
                        if (render instanceof LivingEntityRenderer livingEntityRenderer) {
                            RenderType renderTypeOverride = RenderTypes.energySwirl(livingEntityRenderer.getTextureLocation((LivingEntityRenderState) innerRenderState), uv, uv);
                            render.submit(innerRenderState, poseStackInner, new SubmitNodeCollectorRenderTypeOverride(nodeCollector, renderTypeOverride), cameraRenderState);
                        } else {
                            render.submit(renderState, poseStackInner, nodeCollector, cameraRenderState);
                        }
                    }
                } catch (Exception e) {
                    // Invalid entity, so set as swarm.
                    renderState.spirit.setSwarm(true);
                    renderState.spirit.setPlayerId(""); // Just in case the crash was caused by a player spirit.
                }
            }
        }
    }

    private AvatarRenderState getAvatarRenderState(RenderStateVengeanceSpirit renderState) {
        AvatarRenderState avatarRenderState = new AvatarRenderState();

        avatarRenderState.x = renderState.x;
        avatarRenderState.y = renderState.y;
        avatarRenderState.z = renderState.z;
        avatarRenderState.ageInTicks = renderState.ageInTicks;
        avatarRenderState.boundingBoxWidth = renderState.boundingBoxWidth;
        avatarRenderState.boundingBoxHeight = renderState.boundingBoxHeight;
        avatarRenderState.eyeHeight = renderState.eyeHeight;
        avatarRenderState.distanceToCameraSq = renderState.distanceToCameraSq;
        avatarRenderState.isInvisible = renderState.isInvisible;
        avatarRenderState.isDiscrete = renderState.isDiscrete;
        avatarRenderState.displayFireAnimation = renderState.displayFireAnimation;
        avatarRenderState.passengerOffset = renderState.passengerOffset;
        avatarRenderState.nameTag = renderState.nameTag;
        avatarRenderState.nameTagAttachment = renderState.nameTagAttachment;
        avatarRenderState.leashStates = renderState.leashStates;
        avatarRenderState.partialTick = renderState.partialTick;
        avatarRenderState.bodyRot = renderState.bodyRot;
        avatarRenderState.yRot = renderState.yRot;
        avatarRenderState.xRot = renderState.xRot;

        return avatarRenderState;
    }

    @Override
    public RenderStateVengeanceSpirit createRenderState() {
        return new RenderStateVengeanceSpirit();
    }

    @Override
    public void extractRenderState(EntityVengeanceSpirit entity, RenderStateVengeanceSpirit renderState, float partialTick) {
        super.extractRenderState(entity, renderState, partialTick);

        renderState.spirit = entity;
        renderState.bodyRot = entity.yBodyRot;
        renderState.yRot = entity.yRotO;
        renderState.xRot = entity.xRotO;
    }

    public static class RenderPlayerSpirit extends LivingEntityRenderer<Mob, AvatarRenderState, PlayerModel> {

        private Identifier playerTexture;

        public RenderPlayerSpirit(EntityRendererProvider.Context context) {
            super(context, new PlayerModel(context.bakeLayer(ModelLayers.PLAYER), false), 0.5F);
            this.addLayer(
                    new HumanoidArmorLayer<>(
                            this,
                            ArmorModelSet.bake(
                                    ModelLayers.PLAYER_ARMOR,
                                    context.getModelSet(),
                                    p_446041_ -> new PlayerModel(p_446041_, false)
                            ),
                            context.getEquipmentRenderer()
                    )
            );
            this.addLayer(new ItemInHandLayer<>(this));
            this.addLayer(new ArrowLayer<>(this, context));
            this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getPlayerSkinRenderCache()));
        }

        public void setPlayerTexture(Identifier playerTexture) {
            this.playerTexture = playerTexture;
        }

        @Override
        public AvatarRenderState createRenderState() {
            return new AvatarRenderState();
        }

        @Override
        public Identifier getTextureLocation(AvatarRenderState renderState) {
            return playerTexture;
        }
    }

    // Override the render type buffer so that it always returns buffers with alpha blend
    public static class SubmitNodeCollectorRenderTypeOverride implements SubmitNodeCollector {
        private final SubmitNodeCollector submitNodeCollector;
        private final RenderType renderTypeOverride;

        public SubmitNodeCollectorRenderTypeOverride(SubmitNodeCollector submitNodeCollector, RenderType renderTypeOverride) {
            this.submitNodeCollector = submitNodeCollector;
            this.renderTypeOverride = renderTypeOverride;
        }

        @Override
        public void submitShadow(PoseStack poseStack, float v, List<EntityRenderState.ShadowPiece> list) {
            this.submitNodeCollector.submitShadow(poseStack, v, list);
        }

        @Override
        public void submitNameTag(PoseStack poseStack, @Nullable Vec3 vec3, int i, Component component, boolean b, int i1, double v, CameraRenderState cameraRenderState) {
            this.submitNodeCollector.submitNameTag(poseStack, vec3, i, component, b, i1, v, cameraRenderState);
        }

        @Override
        public void submitText(PoseStack poseStack, float v, float v1, FormattedCharSequence formattedCharSequence, boolean b, Font.DisplayMode displayMode, int i, int i1, int i2, int i3) {
            this.submitNodeCollector.submitText(poseStack, v, v1, formattedCharSequence, b, displayMode, i, i1, i2, i3);
        }

        @Override
        public void submitFlame(PoseStack poseStack, EntityRenderState entityRenderState, Quaternionf quaternionf) {
            this.submitNodeCollector.submitFlame(poseStack, entityRenderState, quaternionf);
        }

        @Override
        public void submitLeash(PoseStack poseStack, EntityRenderState.LeashState leashState) {
            this.submitNodeCollector.submitLeash(poseStack, leashState);
        }

        @Override
        public <S> void submitModel(Model<? super S> model, S s, PoseStack poseStack, RenderType renderType, int i, int i1, int i2, @Nullable TextureAtlasSprite textureAtlasSprite, int i3, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
            this.submitNodeCollector.submitModel(model, s, poseStack, this.renderTypeOverride, i, i1, i2, textureAtlasSprite, i3, crumblingOverlay);
        }

        @Override
        public void submitModelPart(ModelPart modelPart, PoseStack poseStack, RenderType renderType, int i, int i1, @Nullable TextureAtlasSprite textureAtlasSprite, boolean b, boolean b1, int i2, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, int i3) {
            this.submitNodeCollector.submitModelPart(modelPart, poseStack, this.renderTypeOverride, i, i1, textureAtlasSprite, b, b1, i2, crumblingOverlay, i3);
        }

        @Override
        public void submitMovingBlock(PoseStack poseStack, MovingBlockRenderState movingBlockRenderState) {
            this.submitNodeCollector.submitMovingBlock(poseStack, movingBlockRenderState);
        }

        @Override
        public void submitBlockModel(PoseStack poseStack, RenderType renderType, java.util.List<net.minecraft.client.renderer.block.dispatch.BlockStateModelPart> parts, int[] tints, int light, int overlay, int seed) {
            this.submitNodeCollector.submitBlockModel(poseStack, this.renderTypeOverride, parts, tints, light, overlay, seed);
        }

        @Override
        public void submitBreakingBlockModel(PoseStack poseStack, BlockStateModel blockStateModel, long seed, int light) {
            this.submitNodeCollector.submitBreakingBlockModel(poseStack, blockStateModel, seed, light);
        }

        @Override
        public void submitItem(PoseStack poseStack, ItemDisplayContext itemDisplayContext, int i, int i1, int i2, int[] ints, List<BakedQuad> list, ItemStackRenderState.FoilType foilType) {
            this.submitNodeCollector.submitItem(poseStack, itemDisplayContext, i, i1, i2, ints, list, foilType);
        }

        @Override
        public void submitCustomGeometry(PoseStack poseStack, RenderType renderType, SubmitNodeCollector.CustomGeometryRenderer customGeometryRenderer) {
            this.submitNodeCollector.submitCustomGeometry(poseStack, this.renderTypeOverride, customGeometryRenderer);
        }

        @Override
        public void submitParticleGroup(SubmitNodeCollector.ParticleGroupRenderer particleGroupRenderer) {
            this.submitNodeCollector.submitParticleGroup(particleGroupRenderer);
        }

        @Override
        public OrderedSubmitNodeCollector order(int i) {
            return this.submitNodeCollector.order(i);
        }
    }

}
