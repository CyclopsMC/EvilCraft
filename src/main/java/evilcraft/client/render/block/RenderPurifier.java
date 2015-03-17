package evilcraft.client.render.block;

import evilcraft.block.Purifier;
import evilcraft.core.helper.DirectionHelpers;
import evilcraft.core.helper.RenderHelpers;
import evilcraft.fluid.Blood;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;


/**
 * Renderer purifier.
 * @author rubensworks
 *
 */
public class RenderPurifier implements ISimpleBlockRenderingHandler {
    
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
        
        // The rendering of the inside
        TextureAtlasSprite icon = block.getBlockTextureFromSide(2);
        float f4 = 0.125F;
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
        TextureAtlasSprite icon1 = Purifier.getPurifierIcon("inner");
        tessellator.startDrawingQuads();
        tessellator.setNormal(0, -1, 0);
        renderer.renderFaceYPos(block, (double)x, (double)((float)y - 1.0F + 0.25F), (double)z, icon1);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0, 1, 0);
        renderer.renderFaceYNeg(block, (double)x, (double)((float)y + 1.0F - 0.75F), (double)z, icon1);
        tessellator.draw();
        GL11.glPopMatrix();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, BlockPos blockPos,
            Block block, int modelId, RenderBlocks renderer) {
        return renderBlockPurifier((Purifier) block, world, x, y, z, renderer);
    }
    
    private boolean renderBlockPurifier(Purifier purifier, IBlockAccess blockAccess, BlockPos blockPos, RenderBlocks renderer) {
        renderer.renderStandardBlock(purifier, x, y, z);
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(purifier.getMixedBrightnessForBlock(blockAccess, x, y, z));
        float f = 1.0F;
        int l = purifier.colorMultiplier(blockAccess, x, y, z);
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
        TextureAtlasSprite icon = purifier.getBlockTextureFromSide(2);
        f4 = 0.125F;
        renderer.renderFaceXPos(purifier, (double)((float)x - 1.0F + f4), (double)y, (double)z, icon);
        renderer.renderFaceXNeg(purifier, (double)((float)x + 1.0F - f4), (double)y, (double)z, icon);
        renderer.renderFaceZPos(purifier, (double)x, (double)y, (double)((float)z - 1.0F + f4), icon);
        renderer.renderFaceZNeg(purifier, (double)x, (double)y, (double)((float)z + 1.0F - f4), icon);
        TextureAtlasSprite icon1 = Purifier.getPurifierIcon("inner");
        renderer.renderFaceYPos(purifier, (double)x, (double)((float)y - 1.0F + 0.25F), (double)z, icon1);
        renderer.renderFaceYNeg(purifier, (double)x, (double)((float)y + 1.0F - 0.75F), (double)z, icon1);
        int i1 = blockAccess.getBlockMetadata(x, y, z);
        if (i1 > 0) {
            TextureAtlasSprite icon2 = Blood.getInstance().getIcon();

            if (i1 > 3) {
                i1 = 3;
            }

            renderer.renderFaceYPos(purifier, (double)x, (double)((float)y - 1.0F + (6.0F + (float)i1 * 3.0F) / 16.0F), (double)z, icon2);
        }
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
