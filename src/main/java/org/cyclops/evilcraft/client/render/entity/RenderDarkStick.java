package org.cyclops.evilcraft.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.world.entity.EntityType;
import org.cyclops.evilcraft.entity.item.EntityItemDarkStick;

/**
 * Renderer for a dark stick
 *
 * @author rubensworks
 *
 */
public class RenderDarkStick extends EntityRenderer<EntityItemDarkStick, RenderStateDarkStick> {

    private final ItemModelResolver itemModelResolver;

    public RenderDarkStick(EntityRendererProvider.Context renderContext) {
        super(renderContext);
        this.itemModelResolver = renderContext.getItemModelResolver();
    }

    @Override
    public RenderStateDarkStick createRenderState() {
        return new RenderStateDarkStick();
    }

    @Override
    public void extractRenderState(EntityItemDarkStick entity, RenderStateDarkStick state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);

        state.ageInTicks = (float)entity.getAge() + partialTick;
        state.bobOffset = entity.bobOffs;
        state.shouldBob = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(entity.getItem()).shouldBobAsEntity(entity.getItem());
        state.shouldSpread = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(entity.getItem()).shouldSpreadAsEntity(entity.getItem());
        state.extractItemGroupRenderState(entity, entity.getItem(), this.itemModelResolver);
        state.valid = entity.isValid();
        state.angle = entity.getAngle();
    }

    @Override
    public void render(RenderStateDarkStick renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(renderState, poseStack, bufferSource, packedLight);

        float rotation;
        if (renderState.valid) {
            rotation = renderState.angle;
        } else {
            rotation = (((float)renderState.ageInTicks) / 20.0F + renderState.bobOffset) * (180F / (float)Math.PI);
        }

        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        poseStack.mulPose(Axis.YP.rotationDegrees(-90));
        poseStack.mulPose(Axis.XP.rotationDegrees(25));

        // , renderState.isValid() ? -renderState.bobOffset * 20/* to undo hoverstart in ItemRenderer */ : renderState.partialTick
        ((EntityRenderer) Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(EntityType.ITEM))
                .render(renderState, poseStack, bufferSource, packedLight);
    }

}
