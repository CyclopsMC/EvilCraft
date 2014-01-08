package evilcraft.api.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import evilcraft.api.Helpers;
import evilcraft.api.RenderHelpers;
import evilcraft.api.config.configurable.IMultiRenderPassBlock;


/**
 * Got inspiration from OpenBlock's RenderUtils
 * @author rubensworks
 *
 */
public class MultiPassBlockRenderer implements ISimpleBlockRenderingHandler{
    
    public static int ID = RenderingRegistry.getNextAvailableRenderId();
    public CustomRenderBlocks renderBlocks = new CustomRenderBlocks();

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
        renderInventoryBlock(renderer, block, metadata);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        renderBlocks.setWorld(world);
        renderBlocks.setRenderBoundsFromBlock(block);
        boolean visible = false;
        if (block instanceof IMultiRenderPassBlock) {
            IMultiRenderPassBlock blockToRender = (IMultiRenderPassBlock)block;
            blockToRender.setInventoryBlock(false);
            blockToRender.setRenderPass(-1);
            blockToRender.updateTileEntity(world, x, y, z);
            if (renderBlocks.renderStandardBlock(block, x, y, z)) {
                visible = true;
                for (int pass = 0; pass < blockToRender.getRenderPasses(); pass++) {
                    blockToRender.setRenderBlocks(renderBlocks);
                    blockToRender.setRenderPass(pass);
                    renderBlocks.renderStandardBlock(block, x, y, z);
                    resetFacesOnRenderer(renderBlocks);
                }
            }
        }
        return visible;
    }

    @Override
    public boolean shouldRender3DInInventory() {
        return true;
    }

    @Override
    public int getRenderId() {
        return ID;
    }
    
    private void renderInventoryBlock(RenderBlocks renderer, Block block, int metaData) {
        // Init
        Tessellator tessellator = Tessellator.instance;
        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);
        
        // Start GL11
        GL11.glPushMatrix();
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        
        
        if (block instanceof IMultiRenderPassBlock) {
            IMultiRenderPassBlock blockToRender = (IMultiRenderPassBlock)block;
            blockToRender.setInventoryBlock(true);
            for (int pass = 0; pass < blockToRender.getRenderPasses(); pass++) {
                blockToRender.setRenderPass(pass);
                // Loop over sides and render them relative to the given direction.
                for(ForgeDirection renderDirection : Helpers.DIRECTIONS) {
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
                                    metaData
                                    )
                            );
                    tessellator.draw();
                }
            }
        }
        
        GL11.glPopMatrix();
    }
    
    private void resetFacesOnRenderer(RenderBlocks renderer) {
        renderer.uvRotateTop = 0;
        renderer.uvRotateBottom = 0;
        renderer.uvRotateEast = 0;
        renderer.uvRotateNorth = 0;
        renderer.uvRotateSouth = 0;
        renderer.uvRotateTop = 0;
        renderer.uvRotateWest = 0;
        renderer.flipTexture = false;
    }

}
