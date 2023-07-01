package org.cyclops.evilcraft.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.cyclops.evilcraft.entity.item.EntityBroom;
import org.cyclops.evilcraft.entity.item.EntityBroomConfig;

/**
 * Renderer for a broom
 *
 * @author immortaleeb
 *
 */
public class RenderBroom extends EntityRenderer<EntityBroom> {

    public RenderBroom(EntityRendererProvider.Context renderContext, EntityBroomConfig config) {
        super(renderContext);
    }

    protected ItemStack getItemStack(EntityBroom entity) {
        return entity.getBroomStack();
    }

    @Override
    public void render(EntityBroom entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.translate(0, 0.2F, 0);

        // Note: using entity.rotationYaw instead of yaw seems to fix some glitchyness when rendering
        // In case this causes other problems, you can replace it by the yaw again
        float rotationYaw = entity.yRotO + (entity.getYRot() - entity.yRotO) * partialTicks;
        float rotationPitch = entity.xRotO + (entity.getXRot() - entity.xRotO) * partialTicks;
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(-rotationYaw));
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(rotationPitch));

        matrixStackIn.scale(2, 2, 2);
        Minecraft.getInstance().getItemRenderer().renderStatic(getItemStack(entity),
                ItemDisplayContext.FIXED, packedLightIn,
                OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, entity.level(), 0);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityBroom entity) {
        return null;
    }

}
