package org.cyclops.evilcraft.client.render.block;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.TNTMinecartRenderer;
import net.minecraft.client.renderer.entity.TNTRenderer;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

/**
 * Renderer for a primed bomb.
 * @author rubensworks
 *
 */
public class RenderBombPrimed extends TNTRenderer {

    protected final Block block;

    public RenderBombPrimed(EntityRendererManager renderManager, Block block) {
        super(renderManager);
        this.block = block;
    }

    public void render(TNTEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.0D, 0.5D, 0.0D);
        if ((float)entityIn.getLife() - partialTicks + 1.0F < 10.0F) {
            float f = 1.0F - ((float)entityIn.getLife() - partialTicks + 1.0F) / 10.0F;
            f = MathHelper.clamp(f, 0.0F, 1.0F);
            f = f * f;
            f = f * f;
            float f1 = 1.0F + f * 0.3F;
            matrixStackIn.scale(f1, f1, f1);
        }

        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
        matrixStackIn.translate(-0.5D, -0.5D, 0.5D);
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90.0F));
        TNTMinecartRenderer.renderWhiteSolidBlock(this.block.defaultBlockState(), matrixStackIn, bufferIn, packedLightIn, entityIn.getLife() / 5 % 2 == 0);
        matrixStackIn.popPose();
        //super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }
    
}
