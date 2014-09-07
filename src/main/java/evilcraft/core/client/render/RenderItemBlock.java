package evilcraft.core.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

/**
 * Abstract {@link IItemRenderer} for item blocks that is able to render additional stuff next to the standard block.
 * @author rubensworks
 *
 */
public abstract class RenderItemBlock implements IItemRenderer {

	@Override
    public boolean handleRenderType(ItemStack itemStack, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
            ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack itemStack, Object... data) {
    	Block block = Block.getBlockFromItem(itemStack.getItem());
    	
    	GL11.glPushMatrix();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    	
    	preRenderAdditional(type, itemStack, block);

        // Regular block rendering
        if (block.getRenderBlockPass() > 0) {
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        }
        
        GL11.glPushMatrix();
        RenderBlocks.getInstance().renderBlockAsItem(block, itemStack.getItemDamage(), 1.0F);
        GL11.glPopMatrix();

        if (block.getRenderBlockPass() > 0) {
            GL11.glDisable(GL11.GL_BLEND);
        }
        
        postRenderAdditional(type, itemStack, block);
        
        GL11.glPopMatrix();
    }

	protected void preRenderAdditional(ItemRenderType type, ItemStack itemStack, Block block) {
		
	}
	
	protected void postRenderAdditional(ItemRenderType type, ItemStack itemStack, Block block) {
		
	}
	
}
