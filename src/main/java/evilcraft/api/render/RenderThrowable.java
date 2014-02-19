package evilcraft.api.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.entities.item.EntityThrowable;

/**
 * A renderer for a throwable item.
 * @author rubensworks
 *
 */
@SideOnly(Side.CLIENT)
public class RenderThrowable extends Render {
    private Item item;
    
    /**
     * Make a new instance.
     * @param item The item that will be rendered.
     */
    public RenderThrowable(Item item) {
        this.item = item;
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTickTime) {
        renderThrowable((EntityThrowable)entity, x, y, z, yaw, partialTickTime);
    }
    
    private void renderThrowable(EntityThrowable entity, double x, double y, double z, float yaw, float partialTickTime) {
        ItemStack stack = entity.getItemStack();
        int damage = (stack != null) ? stack.getItemDamage() : 0;
        
        int renderPass = 0;
        int numberOfPasses = item.getRenderPasses(damage);
        IIcon icon = item.getIconFromDamageForRenderPass(damage, renderPass);

        if (icon != null) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)x, (float)y, (float)z);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            this.bindEntityTexture(entity);
            Tessellator tessellator = Tessellator.instance;
            
            setColor(stack, renderPass);
            GL11.glPushMatrix();
            this.renderIcon(tessellator, icon);
            GL11.glPopMatrix();
            
            renderPass++;
            
            while (renderPass < numberOfPasses && (icon = item.getIconFromDamageForRenderPass(damage, renderPass)) != null) {
                setColor(stack, renderPass);
                GL11.glPushMatrix();
                this.renderIcon(tessellator, icon);
                GL11.glPopMatrix();
                GL11.glColor3f(1.0F, 1.0F, 1.0F);
                renderPass++;
            }

            GL11.glColor3f(1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
        }
    }
    
    private void setColor(ItemStack stack, int renderPass) {
        if (stack == null)
            return;
        
        int color = stack.getItem().getColorFromItemStack(stack, renderPass);
        
        float red = (float)(color >> 16) / 255.0F;
        float green = (float)(color >> 8 & 255) / 255.0F;
        float blue = (float)(color & 255)/ 255.0F;
        
        GL11.glColor3f(red, green, blue);
    }
    
    private void renderIcon(Tessellator tessellator, IIcon icon) {
        float f = icon.getMinU();
        float f1 = icon.getMaxU();
        float f2 = icon.getMinV();
        float f3 = icon.getMaxV();
        float f4 = 1.0F;
        float f5 = 0.5F;
        float f6 = 0.25F;
        GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        tessellator.addVertexWithUV((double)(0.0F - f5), (double)(0.0F - f6), 0.0D, (double)f, (double)f3);
        tessellator.addVertexWithUV((double)(f4 - f5), (double)(0.0F - f6), 0.0D, (double)f1, (double)f3);
        tessellator.addVertexWithUV((double)(f4 - f5), (double)(f4 - f6), 0.0D, (double)f1, (double)f2);
        tessellator.addVertexWithUV((double)(0.0F - f5), (double)(f4 - f6), 0.0D, (double)f, (double)f2);
        tessellator.draw();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return TextureMap.locationItemsTexture;
    }
}
