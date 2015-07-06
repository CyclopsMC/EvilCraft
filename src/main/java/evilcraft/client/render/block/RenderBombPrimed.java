package evilcraft.client.render.block;

import evilcraft.entity.block.EntityLightningBombPrimed;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderTNTPrimed;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

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

        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        float f2;

        if ((float)entity.fuse - partialTickTime + 1.0F < 10.0F) {
            f2 = 1.0F - ((float)entity.fuse - partialTickTime + 1.0F) / 10.0F;

            if (f2 < 0.0F) {
                f2 = 0.0F;
            }

            if (f2 > 1.0F) {
                f2 = 1.0F;
            }

            f2 *= f2;
            f2 *= f2;
            float f3 = 1.0F + f2 * 0.3F;
            GL11.glScalef(f3, f3, f3);
        }

        f2 = (1.0F - ((float)entity.fuse - partialTickTime + 1.0F) / 100.0F) * 0.8F;
        this.bindEntityTexture(entity);
        blockrendererdispatcher.renderBlockBrightness(block.getDefaultState(), entity.getBrightness(partialTickTime));

        if (entity.fuse / 5 % 2 == 0) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, f2);
            blockrendererdispatcher.renderBlockBrightness(block.getDefaultState(), 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

        GL11.glPopMatrix();
    }
    
    @Override
    protected ResourceLocation func_180563_a(EntityTNTPrimed par1Entity) {
        return TextureMap.locationBlocksTexture;
    }
    
    @Override
    public void doRender(EntityTNTPrimed entity, double x, double y, double z, float yaw, float partialTickTime) {
        this.renderPrimedBomb((EntityLightningBombPrimed)entity, x, y, z, yaw, partialTickTime);
    }
    
}
