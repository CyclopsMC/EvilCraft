package evilcraft.core.config.configurable;

import evilcraft.Reference;
import evilcraft.core.DirectionCorner;
import evilcraft.core.client.render.block.ConnectableIcon;
import evilcraft.core.client.render.block.CustomRenderBlocks;
import evilcraft.core.client.render.block.MultiPassBlockRenderer;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.helper.DirectionHelpers;
import evilcraft.core.helper.RenderHelpers;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.TextureAtlasSpriteRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Block that can hold ExtendedConfigs with connected textures.
 * It will automatically render connected textures when the same blocks are next to it.
 * It uses {@link ConnectableIcon} as the icon that dynamically changes it's appearance.
 * @author rubensworks
 * @see ConnectableIcon
 */
public abstract class ConfigurableBlockConnectedTexture extends ConfigurableBlock {
    
    protected int pass = 0;
    protected CustomRenderBlocks renderer;
    
    protected ConnectableIcon connectableIcon;
    protected TextureAtlasSprite blockIconBorder;
    protected TextureAtlasSprite blockIconCorner;
    protected TextureAtlasSprite blockIconInnerCorner;
    protected TextureAtlasSprite blockIconInventory;

    
    /**
     * Make a new blockState instance.
     * @param eConfig Config for this blockState.
     * @param material Material of this blockState.
     */
    @SuppressWarnings("rawtypes")
    public ConfigurableBlockConnectedTexture(ExtendedConfig eConfig,
            Material material) {
        super(eConfig, material);
    }
    
    /**
     * Get the icon of the background.
     * @return The background icon.
     */
    public TextureAtlasSprite getIconBackground() {
        return blockIcon;
    }

    /**
     * Get the icon for the borders.
     * @return The border icon.
     */
    public TextureAtlasSprite getIconBorders() {
        return blockIconBorder;
    }

    /**
     * Get the icon for the corners.
     * @return The corner icon.
     */
    public TextureAtlasSprite getIconCorners() {
        return blockIconCorner;
    }

    /**
     * Get the inner corner icon.
     * @return The inner corner icon.
     */
    public TextureAtlasSprite getIconInnerCorners() {
        return blockIconInnerCorner;
    }
    
    /**
     * Get the inventory blockState icon.
     * @return The inventory blockState icon.
     * @see ConfigurableBlockConnectedTexture#hasSeperateInventoryBlockIcon()
     */
    public TextureAtlasSprite getIconInventory() {
        return hasSeperateInventoryBlockIcon()?blockIconInventory:blockIcon;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {
        return MultiPassBlockRenderer.ID;
    }
    
    /**
     * If the blockState at the given coordinates should connect to any other blockState.
     * @param world The world.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     * @return If these should connect.
     */
    public boolean shouldConnect(IBlockAccess world, BlockPos blockPos) {
        return true;
    }
    
    /**
     * If this blockState should connect at the given direction.
     * @param world The world.
     * @param side The direction to check the connection with.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     * @return If this blockState should connect at the given direction.
     * @see EnumFacing
     */
    public boolean shouldConnectDirection(IBlockAccess world, EnumFacing side, BlockPos blockPos) {
        return shouldConnect(world, blockPos) && world.getBlock(x + side.offsetX, y + side.offsetY, z + side.offsetZ) == this;
    }
    
    /**
     * If this blockState should connect at the given direction corner.
     * @param world The world.
     * @param side The direction corner to check the connection with.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     * @return If this blockState should connect at the given direction.
     * @see DirectionCorner
     */
    public boolean shouldConnectDirection(IBlockAccess world, DirectionCorner side, BlockPos blockPos) {
        return shouldConnect(world, blockPos) && world.getBlock(x + side.offsetX, y + side.offsetY, z + side.offsetZ) == this;
    }
    
    @Override
    public void setRenderPass(int pass) {
        if(pass < getRenderPasses())
            this.pass = pass;
        else
            this.pass = getRenderPasses() - 1;
    }
    
    @Override
    public boolean shouldRender(int pass) {
    	return pass==0 || pass>=4;
    }
    
    @Override
    public void setRenderBlocks(CustomRenderBlocks renderer) {
        this.renderer = renderer;
    }
    
    @Override
    public CustomRenderBlocks getRenderBlocks() {
        return this.renderer;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(TextureAtlasSpriteRegister iconRegister) {
        super.registerBlockIcons(iconRegister);
        blockIconBorder = iconRegister.registerIcon(getTextureName()+"_border");
        blockIconCorner = iconRegister.registerIcon(getTextureName()+"_corner");
        blockIconInnerCorner = iconRegister.registerIcon(getTextureName()+"_innerCorner");
        if(hasSeperateInventoryBlockIcon())
            blockIconInventory = iconRegister.registerIcon(getTextureName()+"_inventory");
        connectableIcon = new ConnectableIcon(getIconBackground(), getIconBorders(), getIconCorners(), getIconInnerCorners(), getIconInventory());
        
        //TMP
        RenderHelpers.EMPTYICON = iconRegister.registerIcon(Reference.MOD_ID+":empty");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIcon(EnumFacing side, IBlockState blockState) {
        return getIcon(side, meta, pass);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIcon(EnumFacing side, IBlockState blockState, int renderPass) {
        if(this.getRenderBlocks() != null) { // In case for inventoryblock?
            connectableIcon.prepareIcon(side, renderPass, this.getRenderBlocks());
        }
        return connectableIcon;
    }
    
    @Override
    public TextureAtlasSprite getIcon(IBlockAccess world, BlockPos blockPos, EnumFacing side) {
        updateConnections(world, blockPos);// If there are framerate issues, this will propably be the cause.
        return super.getIcon(world, blockPos, side);
    }
    
    @Override
    public int getRenderPasses() {
        return connectableIcon.getRequiredPasses();
    }
    
    private void updateConnections(IBlockAccess world, BlockPos blockPos) {
    	// Regular sides
        for(EnumFacing direction : DirectionHelpers.DIRECTIONS) {
            boolean connect = shouldConnectDirection(world, direction, blockPos);
            connectableIcon.connect(direction, connect);
        }
        // Corner sides
        for(DirectionCorner direction : DirectionHelpers.DIRECTIONS_CORNERS) {
            boolean connect = shouldConnectDirection(world, direction, blockPos);
            connectableIcon.connectCorner(direction, connect);
        }
    }
    
    @Override
    public void setInventoryBlock(boolean isInventoryBlock) {
        this.connectableIcon.setInventoryBlock(isInventoryBlock);
    }
    
    /**
     * If this blockState has a seperate icon for rendering the inventory blockState, false by default.
     * If it is true, the renderer will look for getTextureName()+"_inventory" icon, otherwise
     * the background of the blockState will be taken (without corners, edges and innercorners).
     * @return If this blockState has a seperate icon for rendering the inventory blockState.
     */
    public boolean hasSeperateInventoryBlockIcon() {
        return false;
    }

}
