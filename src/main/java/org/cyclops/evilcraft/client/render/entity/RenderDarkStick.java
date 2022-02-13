package org.cyclops.evilcraft.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import org.cyclops.evilcraft.entity.item.EntityItemDarkStick;
import org.cyclops.evilcraft.entity.item.EntityItemDarkStickConfig;

/**
 * Renderer for a dark stick
 * 
 * @author rubensworks
 *
 */
public class RenderDarkStick extends EntityRenderer<EntityItemDarkStick> {

	public RenderDarkStick(EntityRendererManager renderManager, EntityItemDarkStickConfig config) {
	    super(renderManager);
	}

	@Override
	public void render(EntityItemDarkStick entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        float rotation;
        if (entity.isValid()) {
            rotation = entity.getAngle();
        } else {
            rotation = (((float)entity.getAge()) / 20.0F + entity.bobOffs) * (180F / (float)Math.PI);
        }

        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(rotation));
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-90));
        matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(25));

        ((EntityRenderer) Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(EntityType.ITEM))
                .render(entity, 0, entity.isValid() ? -entity.bobOffs * 20/* to undo hoverstart in ItemRenderer */ : partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

    @Override
    public ResourceLocation getTextureLocation(EntityItemDarkStick entity) {
        return null;
    }

}
