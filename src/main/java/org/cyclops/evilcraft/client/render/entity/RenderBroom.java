package org.cyclops.evilcraft.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
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
    public void submit(RenderStateBroom renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        super.submit(renderState, poseStack, nodeCollector, cameraRenderState);

        poseStack.translate(0, 0.2F, 0);

        // Note: using entity.rotationYaw instead of yaw seems to fix some glitchyness when rendering
        // In case this causes other problems, you can replace it by the yaw again
        float rotationYaw = renderState.yRotO + (renderState.yRot - renderState.yRotO) * renderState.partialTick;
        float rotationPitch = renderState.xRotO + (renderState.xRot - renderState.xRotO) * renderState.partialTick;
        poseStack.mulPose(Axis.YP.rotationDegrees(-rotationYaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(rotationPitch));

        poseStack.scale(2, 2, 2);
        ItemStackRenderState renderStateItemStack = new ItemStackRenderState();
        Minecraft.getInstance().getItemModelResolver().updateForTopItem(renderStateItemStack, getItemStack(renderState), ItemDisplayContext.FIXED, Minecraft.getInstance().level, null, 0);
        renderStateItemStack.submit(poseStack, nodeCollector, 15728880, OverlayTexture.NO_OVERLAY, 0);
    }
}
