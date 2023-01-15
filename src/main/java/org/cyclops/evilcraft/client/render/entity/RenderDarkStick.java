package org.cyclops.evilcraft.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import org.cyclops.evilcraft.entity.item.EntityItemDarkStick;
import org.cyclops.evilcraft.entity.item.EntityItemDarkStickConfig;

/**
 * Renderer for a dark stick
 *
 * @author rubensworks
 *
 */
public class RenderDarkStick extends EntityRenderer<EntityItemDarkStick> {

    public RenderDarkStick(EntityRendererProvider.Context renderContext, EntityItemDarkStickConfig config) {
        super(renderContext);
    }

    @Override
    public void render(EntityItemDarkStick entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        float rotation;
        if (entity.isValid()) {
            rotation = entity.getAngle();
        } else {
            rotation = (((float)entity.getAge()) / 20.0F + entity.bobOffs) * (180F / (float)Math.PI);
        }

        matrixStackIn.mulPose(Axis.YP.rotationDegrees(rotation));
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(-90));
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(25));

        ((EntityRenderer) Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(EntityType.ITEM))
                .render(entity, 0, entity.isValid() ? -entity.bobOffs * 20/* to undo hoverstart in ItemRenderer */ : partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityItemDarkStick entity) {
        return null;
    }

}
