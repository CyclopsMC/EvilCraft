package org.cyclops.evilcraft.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.cyclops.evilcraft.entity.item.EntityBroom;

/**
 * Renderer for a broom
 *
 * @author immortaleeb
 *
 */
public class RenderBroom extends EntityRenderer<EntityBroom, RenderStateBroom> {

    public RenderBroom(EntityRendererProvider.Context renderContext) {
        super(renderContext);
    }

    @Override
    public RenderStateBroom createRenderState() {
        return new RenderStateBroom();
    }

    @Override
    public void extractRenderState(EntityBroom entity, RenderStateBroom reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);

        reusedState.yRotO = entity.yRotO;
        reusedState.xRotO = entity.xRotO;
        reusedState.yRot = entity.getYRot();
        reusedState.xRot = entity.getXRot();
        reusedState.broomStack = entity.getBroomStack();
    }

    protected ItemStack getItemStack(RenderStateBroom renderState) {
        return renderState.broomStack;
    }

    @Override
    public void render(RenderStateBroom renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(renderState, poseStack, bufferSource, packedLight);

        poseStack.translate(0, 0.2F, 0);

        // Note: using entity.rotationYaw instead of yaw seems to fix some glitchyness when rendering
        // In case this causes other problems, you can replace it by the yaw again
        float rotationYaw = renderState.yRotO + (renderState.yRot - renderState.yRotO) * renderState.partialTick;
        float rotationPitch = renderState.xRotO + (renderState.xRot - renderState.xRotO) * renderState.partialTick;
        poseStack.mulPose(Axis.YP.rotationDegrees(-rotationYaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(rotationPitch));

        poseStack.scale(2, 2, 2);
        Minecraft.getInstance().getItemRenderer().renderStatic(getItemStack(renderState),
                ItemDisplayContext.FIXED, packedLight,
                OverlayTexture.NO_OVERLAY, poseStack, bufferSource, Minecraft.getInstance().level, 0);
    }
}
