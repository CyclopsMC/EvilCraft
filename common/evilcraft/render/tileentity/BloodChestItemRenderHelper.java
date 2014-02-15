package evilcraft.render.tileentity;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntityRendererChestHelper;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import evilcraft.blocks.BloodChest;

/**
 * Helper for rendering the {@link BloodChest}.
 * @author rubensworks
 *
 */
public class BloodChestItemRenderHelper extends TileEntityRendererChestHelper {

    @Override
    public void renderChest(Block block, int i, float f) {
        if (block == BloodChest.getInstance()) {
        	TileEntityRendererDispatcher.instance.renderTileEntityAt(BloodChest.getInstance().createTileEntity(null, 0), 0.0D, 0.0D, 0.0D, 0.0F);
        } else {
            super.renderChest(block, i, f);
        }
    }
    
}
