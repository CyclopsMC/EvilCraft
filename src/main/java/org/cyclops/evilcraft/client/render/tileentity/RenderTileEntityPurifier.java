package org.cyclops.evilcraft.client.render.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.Purifier;
import org.cyclops.evilcraft.tileentity.TilePurifier;

/**
 * Renderer for the item inside the {@link Purifier}.
 * 
 * @author rubensworks
 *
 */
public class RenderTileEntityPurifier extends TileEntitySpecialRenderer {
    
    private static final ResourceLocation TEXTURE_BLOOK = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITIES + "blook.png");
    private static final ResourceLocation TEXTURE_ENCHANTEDBOOK = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITIES + "enchantedBook.png");
    private ModelBook enchantmentBook = new ModelBook();
	
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTickTime, int partialDamage) {
	    TilePurifier tile = (TilePurifier) tileEntity;
	    
	    if(tile != null) {
            if(tile.getBookItem() != null) {
                renderBook(tile, tile.getWorld(), tile.getBookItem(), x, y + 0.4, z, partialTickTime);
            }
        }
	    
	    GlStateManager.pushMatrix();
	    float var10 = (float) (x - 0.5F);
        float var11 = (float) (y - 0.5F);
        float var12 = (float) (z - 0.5F);
        GlStateManager.translate(var10, var11, var12);
	    
	    
        if(tile != null) {
            if(tile.getPurifyItem() != null) {
                renderItem(tile.getWorld(), tile.getPurifyItem(), tile.getRandomRotation());
            }
        }
        
        GlStateManager.popMatrix();
	}
	
	private void renderItem(World world, ItemStack itemStack, float rotation) {
        GlStateManager.pushMatrix();
        if (itemStack.getItem() instanceof ItemBlock) {
            GlStateManager.translate(1F, 1.2F, 1F);
            GlStateManager.scale(1.2F, 1.2F, 1.2F);
        } else {
            GlStateManager.translate(1F, 1.2F, 1F);
            GlStateManager.rotate(25F, 1, 0, 0);
            GlStateManager.rotate(25F, 0, 1, 0);
            GlStateManager.rotate(rotation, 0, 1, 0);
        }
        
        GlStateManager.pushAttrib();
        RenderHelper.enableStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().renderItemModel(itemStack);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popAttrib();

        GlStateManager.popMatrix();
    }
	
	private void renderBook(TilePurifier tile, World world, ItemStack itemStack, double x, double y, double z, float partialTickTime) {
	    GlStateManager.pushMatrix();
        GlStateManager.translate((float)x + 0.5F, (float)y + 0.75F, (float)z + 0.5F);
        float tick = (float)tile.tickCount + partialTickTime;
        GlStateManager.translate(0.0F, 0.1F + MathHelper.sin(tick * 0.1F) * 0.01F, 0.0F);
        float speedUp;

        for (speedUp = tile.bookRotation2 - tile.bookRotationPrev; speedUp >= (float)Math.PI; speedUp -= ((float)Math.PI * 2F)) { }

        while (speedUp < -(float)Math.PI) {
            speedUp += ((float)Math.PI * 2F);
        }

        float rotation = tile.bookRotationPrev + speedUp * partialTickTime;
        GlStateManager.rotate(-rotation * 180.0F / (float) Math.PI, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(80.0F, 0.0F, 0.0F, 1.0F);
        
        if(itemStack.getItem() == TilePurifier.ALLOWED_BOOK)
            this.bindTexture(TEXTURE_BLOOK);
        else
            this.bindTexture(TEXTURE_ENCHANTEDBOOK);

        GlStateManager.enableCull();
        this.enchantmentBook.render(null, tick, 0, 0, 0, 0.0F, 0.0625F);
        GlStateManager.popMatrix();
	}

}
