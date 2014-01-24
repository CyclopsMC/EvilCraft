package evilcraft.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.ChestItemRenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import evilcraft.blocks.BloodChest;

public class BloodChestItemRenderHelper extends ChestItemRenderHelper {

    @Override
    public void renderChest(Block block, int i, float f) {
        if (block == BloodChest.getInstance()) {
            TileEntityRenderer.instance.renderTileEntityAt(BloodChest.getInstance().createTileEntity(null, 0), 0.0D, 0.0D, 0.0D, 0.0F);
        } else {
            super.renderChest(block, i, f);
        }
    }
    
}
