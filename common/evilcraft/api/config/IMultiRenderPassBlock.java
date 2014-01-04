package evilcraft.api.config;

import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import evilcraft.api.render.CustomRenderBlocks;

public interface IMultiRenderPassBlock {
    
    public Icon getIcon(int side, int meta, int renderPass);
    public int getRenderPasses();
    public void setRenderPass(int pass);
    public void setRenderBlocks(CustomRenderBlocks renderer);
    public CustomRenderBlocks getRenderBlocks();
    public void updateTileEntity(IBlockAccess world, int x, int y, int z);
    public void setInventoryBlock(boolean isInventoryBlock);
    
}
