package evilcraft.client.render.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import evilcraft.block.EnvironmentalAccumulator;
import evilcraft.core.helper.DirectionHelpers;
import evilcraft.core.helper.RenderHelpers;


/**
 * Renderer purifier.
 * @author rubensworks
 *
 */
public class RenderEnvironmentalAccumulator implements ISimpleBlockRenderingHandler {
    
    /**
     * The ID for this renderer.
     */
    public static final int ID = RenderingRegistry.getNextAvailableRenderId();

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID,
            RenderBlocks renderer) {
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
        renderInventoryBlock(renderer, block, metadata);
    }

    private void renderInventoryBlock(RenderBlocks renderer, Block block, int metadata) {
        // Init
        Tessellator tessellator = Tessellator.instance;
        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);
        
        // Start GL11
        GL11.glPushMatrix();
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        
        // Loop over sides and render them relative to the given direction.
        for(ForgeDirection renderDirection : DirectionHelpers.DIRECTIONS) {
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
        
        // The rendering of the inside
        IIcon icon = block.getBlockTextureFromSide(ForgeDirection.DOWN.ordinal());
        float f4 = 0.300F;
        double x = 0;
        double y = 0;
        double z = 0;
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1, 0, 0);
        renderer.renderFaceXPos(block, (double)((float)x - 1.0F + f4), (double)y, (double)z, icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1, 0, 0);
        renderer.renderFaceXNeg(block, (double)((float)x + 1.0F - f4), (double)y, (double)z, icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0, 0, -1);
        renderer.renderFaceZPos(block, (double)x, (double)y, (double)((float)z - 1.0F + f4), icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0, 0, 1);
        renderer.renderFaceZNeg(block, (double)x, (double)y, (double)((float)z + 1.0F - f4), icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0, -1, 0);
        renderer.renderFaceYPos(block, (double)x, (double)((float)y - 1.0F + 0.55F), (double)z, icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0, 1, 0);
        renderer.renderFaceYNeg(block, (double)x, (double)((float)y + 1.0F - 0.45F), (double)z, icon);
        tessellator.draw();
        GL11.glPopMatrix();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
            Block block, int modelId, RenderBlocks renderer) {
        return renderEnvironmentalAccumulator((EnvironmentalAccumulator) block, world, x, y, z, renderer);
    }
    
    private boolean renderEnvironmentalAccumulator(EnvironmentalAccumulator envirAcc, IBlockAccess blockAccess, int x, int y, int z, RenderBlocks renderer) {
        renderer.renderStandardBlock(envirAcc, x, y, z);
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(envirAcc.getMixedBrightnessForBlock(blockAccess, x, y, z));
        float f = 1.0F;
        int l = envirAcc.colorMultiplier(blockAccess, x, y, z);
        float f1 = (float)(l >> 16 & 255) / 255.0F;
        float f2 = (float)(l >> 8 & 255) / 255.0F;
        float f3 = (float)(l & 255) / 255.0F;
        float f4;

        if (EntityRenderer.anaglyphEnable) {
            float f5 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
            f4 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
            float f6 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
            f1 = f5;
            f2 = f4;
            f3 = f6;
        }

        tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
        IIcon icon = envirAcc.getBlockTextureFromSide(ForgeDirection.DOWN.ordinal());
        f4 = 0.300F;
        renderer.renderFaceXPos(envirAcc, (double)((float)x - 1.0F + f4), (double)y, (double)z, icon);
        renderer.renderFaceXNeg(envirAcc, (double)((float)x + 1.0F - f4), (double)y, (double)z, icon);
        renderer.renderFaceZPos(envirAcc, (double)x, (double)y, (double)((float)z - 1.0F + f4), icon);
        renderer.renderFaceZNeg(envirAcc, (double)x, (double)y, (double)((float)z + 1.0F - f4), icon);
        renderer.renderFaceYPos(envirAcc, (double)x, (double)((float)y - 1.0F + 0.55F), (double)z, icon);
        renderer.renderFaceYNeg(envirAcc, (double)x, (double)((float)y + 1.0F - 0.45F), (double)z, icon);
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
