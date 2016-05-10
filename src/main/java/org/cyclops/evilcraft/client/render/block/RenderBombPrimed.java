package org.cyclops.evilcraft.client.render.block;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderTNTPrimed;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.cyclops.evilcraft.entity.block.EntityLightningBombPrimed;

/**
 * Renderer for a primed bomb.
 * @author rubensworks
 *
 */
public class RenderBombPrimed extends RenderTNTPrimed {

    protected final Block block;
    
    /**
     * Make a new RenderBombPrimed for a certain blockState disguise.
     * @param renderManager The render manager
     * @param block The block
     */
    public RenderBombPrimed(RenderManager renderManager, Block block) {
        super(renderManager);
        this.block = block;
    }
    
    private void renderPrimedBomb(EntityLightningBombPrimed entity, double x, double y, double z, float yaw, float partialTickTime) {
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y + 0.5F, (float)z);
        float f2;

        if ((float)entity.getFuse() - partialTickTime + 1.0F < 10.0F)
        {
            f2 = 1.0F - ((float)entity.getFuse() - partialTickTime + 1.0F) / 10.0F;
            f2 = MathHelper.clamp_float(f2, 0.0F, 1.0F);
            f2 *= f2;
            f2 *= f2;
            float f3 = 1.0F + f2 * 0.3F;
            GlStateManager.scale(f3, f3, f3);
        }

        f2 = (1.0F - ((float)entity.getFuse() - partialTickTime + 1.0F) / 100.0F) * 0.8F;
        this.bindEntityTexture(entity);
        GlStateManager.translate(-0.5F, -0.5F, 0.5F);
        blockrendererdispatcher.renderBlockBrightness(block.getDefaultState(), entity.getBrightness(partialTickTime));
        GlStateManager.translate(0.0F, 0.0F, 1.0F);

        if (entity.getFuse() / 5 % 2 == 0)
        {
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 772);
            GlStateManager.color(1.0F, 1.0F, 1.0F, f2);
            GlStateManager.doPolygonOffset(-3.0F, -3.0F);
            GlStateManager.enablePolygonOffset();
            blockrendererdispatcher.renderBlockBrightness(block.getDefaultState(), 1.0F);
            GlStateManager.doPolygonOffset(0.0F, 0.0F);
            GlStateManager.disablePolygonOffset();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
        }

        GlStateManager.popMatrix();
    }
    
    @Override
    protected ResourceLocation getEntityTexture(EntityTNTPrimed par1Entity) {
        return TextureMap.locationBlocksTexture;
    }
    
    @Override
    public void doRender(EntityTNTPrimed entity, double x, double y, double z, float yaw, float partialTickTime) {
        this.renderPrimedBomb((EntityLightningBombPrimed)entity, x, y, z, yaw, partialTickTime);
    }
    
}
