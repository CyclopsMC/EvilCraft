package org.cyclops.evilcraft.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.cyclops.evilcraft.entity.item.EntityBroom;
import org.cyclops.evilcraft.entity.item.EntityBroomConfig;

/**
 * Renderer for a broom
 * 
 * @author immortaleeb
 *
 */
public class RenderBroom extends EntityRenderer<EntityBroom> {

	public RenderBroom(EntityRendererManager renderManager, EntityBroomConfig config) {
	    super(renderManager);
	}

    protected ItemStack getItemStack(EntityBroom entity) {
        return entity.getBroomStack();
    }

	@Override
	public void render(EntityBroom entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.translate(0, 0.2F, 0);

        // Note: using entity.rotationYaw instead of yaw seems to fix some glitchyness when rendering
        // In case this causes other problems, you can replace it by the yaw again
        float rotationYaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
        float rotationPitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        GlStateManager.rotatef(-rotationYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(rotationPitch, 1.0F, 0.0F, 0.0F);

        ((EntityRenderer) Minecraft.getInstance().getRenderManager().renderers.get(EntityType.ITEM))
                .render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

    @Override
    public ResourceLocation getEntityTexture(EntityBroom entity) {
        return null;
    }

}
