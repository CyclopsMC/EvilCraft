package org.cyclops.evilcraft.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
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
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-rotationYaw));
        matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(rotationPitch));

        matrixStackIn.scale(2, 2, 2);
        Minecraft.getInstance().getItemRenderer().renderStatic(getItemStack(entity),
                ItemTransforms.TransformType.FIXED, packedLightIn,
                OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, 0);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityBroom entity) {
        return null;
    }

}
