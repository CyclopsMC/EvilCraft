package evilcraft.client.render.block;

import evilcraft.core.helper.DirectionHelpers;
import evilcraft.core.helper.RenderHelpers;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;


/**
 * Renderer dark tank.
 * @author rubensworks
 *
 */
public class RenderDarkTank implements ISimpleBlockRenderingHandler {
    
    /**
     * The ID for this renderer.
     */
    public static final int ID = RenderingRegistry.getNextAvailableRenderId();

    @Override
    public void renderInventoryBlock(Block block, IBlockState blockStatedata, int modelID,
            RenderBlocks renderer) {
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
        renderInventoryBlock(renderer, block, metadata);
    }

    private void renderInventoryBlock(RenderBlocks renderer, Block block, IBlockState blockStatedata) {
        // Init
        Tessellator tessellator = Tessellator.instance;
        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);
        
        // Start GL11
        GL11.glPushMatrix();
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        
        // Make sure both sides are rendered
        GL11.glDisable(GL11.GL_CULL_FACE);
        
        GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        // Loop over sides and render them relative to the given direction.
        for(EnumFacing renderDirection : DirectionHelpers.DIRECTIONS) {
            tessellator.startDrawingQuads();
            tessellator.setNormal(
                    renderDirection.offsetX,
                    renderDirection.offsetY,
                    renderDirection.offsetZ
                    );
            RenderHelpers.renderFaceDirection(
                    renderDirection,
                    renderer,
                    block,
                    0.0D, 0.0D, 0.0D,
                    renderer.getBlockIconFromSideAndMetadata(
                            block,
                            renderDirection.ordinal(),
                            metadata
                            )
                    );
            tessellator.draw();
        }
        
        //GL11.glEnable(GL11.GL_CULL_FACE); // Re-enabling this gives strange rendering errors...
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, BlockPos blockPos,
            Block block, int modelId, RenderBlocks renderer) {
    	// We render the blockState two times, but with swapped render blockState bounds, so it will render the backside as well.
    	// Disabling CULL_FACE does not work since the renderer doesn't render immediately, because of that other renderers might
    	// disable CULL_FACE again later.
    	
    	// Render like normal
    	block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);
    	renderer.renderStandardBlock(block, x, y, z);
    	
    	// Render opposite sides
    	RenderHelpers.setInvertedRenderBounds(renderer, block);
		renderer.renderStandardBlock(block, x, y, z);
		
        return true;
    }
    
    @Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

    @Override
    public int getRenderId() {
        return ID;
    }
    
}
