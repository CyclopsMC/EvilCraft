package evilcraft.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderTNTPrimed;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import evilcraft.entities.block.EntityLightningBombPrimed;

public class RenderBombPrimed extends RenderTNTPrimed{
    
    protected RenderBlocks blockRenderer = new RenderBlocks();
    protected Block block;
    
    /**
     * Make a new RenderBombPrimed for a certain block disguise.
     * @param block The block to render for this entity
     */
    public RenderBombPrimed(Block block) {
        this.block = block;
    }

    
    public void renderPrimedBomb(EntityLightningBombPrimed entity, double par2, double par4, double par6, float par8, float par9) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)par2, (float)par4, (float)par6);
        float f2;

        if ((float)entity.fuse - par9 + 1.0F < 10.0F) {
            f2 = 1.0F - ((float)entity.fuse - par9 + 1.0F) / 10.0F;

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

        f2 = (1.0F - ((float)entity.fuse - par9 + 1.0F) / 100.0F) * 0.8F;
        this.bindEntityTexture(entity);
        this.blockRenderer.renderBlockAsItem(block, 0, entity.getBrightness(par9));

        if (entity.fuse / 5 % 2 == 0) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, f2);
            this.blockRenderer.renderBlockAsItem(block, 0, 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

        GL11.glPopMatrix();
    }
    
    @Override
    protected ResourceLocation getEntityTexture(Entity par1Entity) {
        return TextureMap.locationBlocksTexture;
    }
    
    @Override
    public void doRender(Entity entity, double par2, double par4, double par6, float par8, float par9) {
        this.renderPrimedBomb((EntityLightningBombPrimed)entity, par2, par4, par6, par8, par9);
    }
    
}
