package org.cyclops.evilcraft.client.render.item;

import org.cyclops.evilcraft.entity.item.EntityItemDarkStick;
import org.cyclops.evilcraft.entity.item.EntityItemDefinedRotation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

import java.util.Random;

/**
 * Renderer for a dark stick entity item.
 * @author rubensworks
 *
 */
public class RenderEntityItemDarkStick implements IItemRenderer {

    private final Random random = new Random();
    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type == ItemRenderType.ENTITY;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return helper != ItemRendererHelper.ENTITY_ROTATION;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        if(data[1] instanceof EntityItemDefinedRotation) {
            EntityItemDarkStick entity = (EntityItemDarkStick) data[1];

            GL11.glPushMatrix();

            float rotation;
            if(entity.isValid()) {
                rotation = entity.getAngle();
            } else {
                rotation = (((float)entity.age) / 20.0F + entity.hoverStart) * (180F / (float)Math.PI);

            }
            GL11.glRotatef(rotation, 0, 1, 0);
            GL11.glRotatef(45F, 0, 1, 0);
            GL11.glRotatef(25F, 1, 0, 0);
            GL11.glScalef(2F, 2F, 2F);

            renderDroppedItem(entity, item.getIconIndex(), 1, 1, 0, 1, 1, 1);

            GL11.glPopMatrix();
        }
    }

    // Copied (and adapted) from RenderItem
    private void renderDroppedItem(EntityItem entity, IIcon icon, int p_77020_3_, float p_77020_4_, float p_77020_5_, float p_77020_6_, float p_77020_7_, int pass) {
        RenderManager renderManager = RenderManager.instance;
        Tessellator tessellator = Tessellator.instance;

        if (icon == null)
        {
            TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
            ResourceLocation resourcelocation = texturemanager.getResourceLocation(entity.getEntityItem().getItemSpriteNumber());
            icon = ((TextureMap)texturemanager.getTexture(resourcelocation)).getAtlasSprite("missingno");
        }

        float f14 = ((IIcon)icon).getMinU();
        float f15 = ((IIcon)icon).getMaxU();
        float f4 = ((IIcon)icon).getMinV();
        float f5 = ((IIcon)icon).getMaxV();
        float f6 = 1.0F;
        float f7 = 0.5F;
        float f8 = 0.25F;
        float f10;

        if (renderManager.options.fancyGraphics)
        {
            GL11.glPushMatrix();

            float f9 = 0.0625F;
            f10 = 0.021875F;
            ItemStack itemstack = entity.getEntityItem();
            int j = itemstack.stackSize;
            byte b0;

            if (j < 2)
            {
                b0 = 1;
            }
            else if (j < 16)
            {
                b0 = 2;
            }
            else if (j < 32)
            {
                b0 = 3;
            }
            else
            {
                b0 = 4;
            }

            b0 = RenderItem.getInstance().getMiniItemCount(itemstack, b0);

            GL11.glTranslatef(-f7, -f8, -((f9 + f10) * (float)b0 / 2.0F));

            for (int k = 0; k < b0; ++k)
            {
                // Makes items offset when in 3D, like when in 2D, looks much better. Considered a vanilla bug...
                if (k > 0)
                {
                    GL11.glTranslatef(0, 0, f9 + f10);
                }
                else
                {
                    GL11.glTranslatef(0f, 0f, f9 + f10);
                }

                if (itemstack.getItemSpriteNumber() == 0)
                {
                    Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
                }
                else
                {
                    Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
                }

                GL11.glColor4f(p_77020_5_, p_77020_6_, p_77020_7_, 1.0F);
                ItemRenderer.renderItemIn2D(tessellator, f15, f4, f14, f5, ((IIcon) icon).getIconWidth(), ((IIcon) icon).getIconHeight(), f9);

                if (itemstack.hasEffect(pass))
                {
                    GL11.glDepthFunc(GL11.GL_EQUAL);
                    GL11.glDisable(GL11.GL_LIGHTING);
                    renderManager.renderEngine.bindTexture(RES_ITEM_GLINT);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
                    float f11 = 0.76F;
                    GL11.glColor4f(0.5F * f11, 0.25F * f11, 0.8F * f11, 1.0F);
                    GL11.glMatrixMode(GL11.GL_TEXTURE);
                    GL11.glPushMatrix();
                    float f12 = 0.125F;
                    GL11.glScalef(f12, f12, f12);
                    float f13 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
                    GL11.glTranslatef(f13, 0.0F, 0.0F);
                    GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
                    ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, f9);
                    GL11.glPopMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(f12, f12, f12);
                    f13 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
                    GL11.glTranslatef(-f13, 0.0F, 0.0F);
                    GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
                    ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, f9);
                    GL11.glPopMatrix();
                    GL11.glMatrixMode(GL11.GL_MODELVIEW);
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glEnable(GL11.GL_LIGHTING);
                    GL11.glDepthFunc(GL11.GL_LEQUAL);
                }
            }

            GL11.glPopMatrix();
        }
        else
        {
            for (int l = 0; l < p_77020_3_; ++l)
            {
                GL11.glPushMatrix();

                if (l > 0)
                {
                    f10 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float f16 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float f17 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    GL11.glTranslatef(f10, f16, f17);
                }

                GL11.glColor4f(p_77020_5_, p_77020_6_, p_77020_7_, 1.0F);
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 1.0F, 0.0F);
                tessellator.addVertexWithUV((double)(0.0F - f7), (double)(0.0F - f8), 0.0D, (double)f14, (double)f5);
                tessellator.addVertexWithUV((double)(f6 - f7), (double)(0.0F - f8), 0.0D, (double)f15, (double)f5);
                tessellator.addVertexWithUV((double)(f6 - f7), (double)(1.0F - f8), 0.0D, (double)f15, (double)f4);
                tessellator.addVertexWithUV((double)(0.0F - f7), (double)(1.0F - f8), 0.0D, (double)f14, (double)f4);
                tessellator.draw();
                GL11.glPopMatrix();
            }
        }
    }
}
