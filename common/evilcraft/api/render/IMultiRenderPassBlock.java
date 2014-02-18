package evilcraft.api.render;

import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

/**
 * Interface for blocks that can render with connected textures, use together
 * with {@link ConnectableIcon}.
 * @author rubensworks
 * @see ConnectableIcon
 */
public interface IMultiRenderPassBlock {
    
    /**
     * Get the icon for the given parameters.
     * @param side The side to render for.
     * @param meta The metadata of the block that will be rendered.
     * @param renderPass The renderpass.
     * @return The icon.
     */
    public IIcon getIcon(int side, int meta, int renderPass);
    /**
     * Get the amount of required render passes.
     * @return Required render passes.
     */
    public int getRenderPasses();
    /**
     * Set the current pass to render at.
     * @param pass The new render pass.
     */
    public void setRenderPass(int pass);
    /**
     * If the block should be rendered in this pass.
     * @param pass The pass to check.
     * @return If it should be rendered.
     */
    public boolean shouldRender(int pass);
    /**
     * Set the {@link CustomRenderBlocks} instance.
     * @param renderer The {@link CustomRenderBlocks} instance.
     */
    public void setRenderBlocks(CustomRenderBlocks renderer);
    /**
     * Get the {@link CustomRenderBlocks} instance.
     * @return The {@link CustomRenderBlocks} instance.
     */
    public CustomRenderBlocks getRenderBlocks();
    /**
     * Update the tile entity (for updating the icon) at the given coordinates.
     * @param world The world.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     */
    public void updateTileEntity(IBlockAccess world, int x, int y, int z);
    /**
     * Define whether or not the current rendering is for an inventory block.
     * @param isInventoryBlock
     */
    public void setInventoryBlock(boolean isInventoryBlock);
    
}
