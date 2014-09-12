package evilcraft.client.render.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.RenderingRegistry;
import evilcraft.client.render.tileentity.RenderTileEntityDarkTank;
import evilcraft.core.block.IBlockTank;
import evilcraft.core.client.render.item.RenderItemBlock;


/**
 * Render the dark tank as item.
 * @author rubensworks
 *
 */
public class RenderItemDarkTank extends RenderItemBlock {

    /**
     * The ID for this renderer.
     */
    public static int ID = RenderingRegistry.getNextAvailableRenderId();
    
    @Override
	protected void preRenderAdditional(ItemRenderType type, ItemStack itemStack, Block block) {
    	GL11.glPushMatrix();
    	if(type != ItemRenderType.ENTITY) {
    		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
    	}
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_CULL_FACE);
        
        // Correct color & lighting
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
		// Render the fluid sides
        IBlockTank tank = (IBlockTank) block;
        if(itemStack.getTagCompound() != null) {
        	FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(itemStack.getTagCompound().getCompoundTag(tank.getTankNBTName()));
            if(fluidStack != null) {
            	int capacity = tank.getTankCapacity(itemStack);
            	double height = Math.min(1.0D, ((double) fluidStack.amount / (double) capacity)) / 1.01D;
            	RenderTileEntityDarkTank.renderFluidSides(height, fluidStack);
            }
        }
        
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
	}

}
